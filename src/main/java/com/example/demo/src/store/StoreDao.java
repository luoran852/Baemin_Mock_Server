package com.example.demo.src.store;

import com.example.demo.src.store.model.*;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StoreDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // [GET] 가게 리스트 조회 API
    public List<GetStoreListRes> getStoreLists(int type, int category, int sort) {
        String getContentsQuery = "";
        int getContentsParams1 = 0, getContentsParams2 = 0;

        // 배달 빠른 순 가게리스트 정렬
        if (sort == 1) {
            getContentsQuery = "select S.idx storeIdx, storePosterUrl, storeName, isPacking, isNew, isCoupon, S.rating, reviewNum, deliMinOrderPrice, deliveryTip, deliveryTime\n" +
                    "from Store S\n" +
                    "join (select R.storeIdx, count(storeIdx) reviewNum\n" +
                    "from Review R\n" +
                    "group by storeIdx) reviewNum on reviewNum.storeIdx = S.idx,\n" +
                    "     Category Ca join CategoryStore CS on Ca.idx = CS.idx,\n" +
                    "     Type T join TypeStore TS on T.idx = TS.idx\n" +
                    "where T.idx = ? and Ca.idx = ?\n" +
                    "order by deliveryTime asc";

            getContentsParams1 = type;
            getContentsParams2 = category;
        }

        // 배달팁 낮은순 가게리스트 정렬
        if (sort == 2) {
            getContentsQuery = "select S.idx storeIdx, storePosterUrl, storeName, isPacking, isNew, isCoupon, S.rating, reviewNum, deliMinOrderPrice, deliveryTip, deliveryTime\n" +
                    "from Store S\n" +
                    "join (select R.storeIdx, count(storeIdx) reviewNum\n" +
                    "from Review R\n" +
                    "group by storeIdx) reviewNum on reviewNum.storeIdx = S.idx,\n" +
                    "     Category Ca join CategoryStore CS on Ca.idx = CS.idx,\n" +
                    "     Type T join TypeStore TS on T.idx = TS.idx\n" +
                    "where T.idx = ? and Ca.idx = ?\n" +
                    "order by deliveryTip asc";

            getContentsParams1 = type;
            getContentsParams2 = category;
        }

        // 기본순 가게리스트 정렬
        if (sort == 3) {
            getContentsQuery = "select S.idx storeIdx, storePosterUrl, storeName, isPacking, isNew, isCoupon, S.rating, reviewNum, deliMinOrderPrice, deliveryTip, deliveryTime\n" +
                    "from Store S\n" +
                    "join (select R.storeIdx, count(storeIdx) reviewNum\n" +
                    "from Review R\n" +
                    "group by storeIdx) reviewNum on reviewNum.storeIdx = S.idx,\n" +
                    "     Category Ca join CategoryStore CS on Ca.idx = CS.idx,\n" +
                    "     Type T join TypeStore TS on T.idx = TS.idx\n" +
                    "where T.idx = ? and Ca.idx = ?";

            getContentsParams1 = type;
            getContentsParams2 = category;
        }


        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetStoreListRes(
                        rs.getInt("storeIdx"),
                        rs.getString("storePosterUrl"),
                        rs.getString("storeName"),
                        rs.getInt("isPacking"),
                        rs.getInt("isNew"),
                        rs.getInt("isCoupon"),
                        rs.getFloat("rating"),
                        rs.getInt("reviewNum"),
                        getMainMenu(rs.getInt("storeIdx")),
                        rs.getInt("deliMinOrderPrice"),
                        rs.getInt("deliveryTip"),
                        rs.getInt("deliveryTime")),
                getContentsParams1, getContentsParams2
        );
    }

    // [GET] 가게리스트 메인메뉴 조회 API (추가 쿼리)
    public List<String> getMainMenu(int storeIdx){
        String getContentsQuery = "select F.foodTxt\n" +
                "from Store S\n" +
                "   join Food F on S.idx = F.menuIdx\n" +
                "         join FoodTypeFood FTF on F.idx = FTF.foodIdx\n" +
                "where S.idx = ?\n" +
                "group by F.idx\n" +
                "limit 2";
        int getContentsParams = storeIdx;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) ->
                        rs.getString("foodTxt"),
                getContentsParams);
    }

    // [GET] 가게 정보 조회 API
    public GetStoreInfoRes getStoreInfo(int storeIdx){
        String getContentsQuery = "select storeName, foodPosterUrl, S.rating, reviewNum, bossCommentNum, keepNum, deliMinOrderPrice, deliPayType, deliveryTime, deliveryTip, packMinOrderPrice, howToUse, cookingTime, locationInfo, storeDistance, packPayType, storeInfo, storeInfoImgUrl, storeFullName, operatingTime, holiday, storePhoneNum, deliveryArea, guideAndBenefits, RepresentativeName, storeNum\n" +
                "from Store S\n" +
                "join (select R.storeIdx, count(storeIdx) reviewNum\n" +
                "from Review R\n" +
                "group by storeIdx) reviewNum on reviewNum.storeIdx = S.idx\n" +
                "join (select BC.storeIdx, count(storeIdx) bossCommentNum\n" +
                "from BossComment BC\n" +
                "group by storeIdx) bossCommentNum on S.idx = bossCommentNum.storeIdx\n" +
                "join (select K.storeIdx, count(storeIdx) keepNum\n" +
                "from Keep K\n" +
                "group by storeIdx) keepNum on S.idx = keepNum.storeIdx\n" +
                "where S.idx = ?";
        int getContentsParams = storeIdx;
        return this.jdbcTemplate.queryForObject(getContentsQuery,
                (rs, rowNum) -> new GetStoreInfoRes(
                        rs.getString("storeName"),
                        rs.getString("foodPosterUrl"),
                        rs.getFloat("rating"),
                        rs.getInt("reviewNum"),
                        rs.getInt("bossCommentNum"),
                        rs.getInt("keepNum"),
                        rs.getInt("deliMinOrderPrice"),
                        rs.getString("deliPayType"),
                        rs.getInt("deliveryTime"),
                        rs.getInt("deliveryTip"),
                        rs.getInt("packMinOrderPrice"),
                        rs.getString("howToUse"),
                        rs.getInt("cookingTime"),
                        rs.getString("locationInfo"),
                        rs.getFloat("storeDistance"),
                        rs.getString("packPayType"),
                        rs.getString("storeInfo"),
                        rs.getString("storeInfoImgUrl"),
                        rs.getString("storeFullName"),
                        rs.getString("operatingTime"),
                        rs.getString("holiday"),
                        rs.getString("storePhoneNum"),
                        rs.getString("deliveryArea"),
                        rs.getString("guideAndBenefits"),
                        rs.getString("RepresentativeName"),
                        rs.getString("storeNum")),
                getContentsParams);
    }

    // [GET] 가게 메뉴 조회 API
    public List<GetStoreMenuRes> getStoreMenu(int storeIdx){
        String getContentsQuery = "select F.idx foodIdx, F.foodTxt, FTF.foodTypeIdx, F.foodComment, F.foodPrice, F.foodImgUrl, F.isPopular, F.isSoldOut, F.isAlcohol\n" +
                "from Food F\n" +
                "join FoodTypeFood FTF on F.idx = FTF.foodIdx\n" +
                "where F.menuIdx = ?\n" +
                "order by FTF.foodTypeIdx asc";
        int getContentsParams = storeIdx;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetStoreMenuRes(
                        getMenuInfo(storeIdx),
                        rs.getInt("foodIdx"),
                        rs.getString("foodTxt"),
                        rs.getInt("foodTypeIdx"),
                        rs.getString("foodComment"),
                        rs.getInt("foodPrice"),
                        rs.getString("foodImgUrl"),
                        rs.getInt("isPopular"),
                        rs.getInt("isSoldOut"),
                        rs.getInt("isAlcohol")),
                getContentsParams);
    }

    // [GET] 가게 메뉴공지 조회 API (추가쿼리)
    public GetMenuInfoRes getMenuInfo(int storeIdx){
        String getContentsQuery = "select M.menuInfo, M.foodTypeNum, M.foodOrigin\n" +
                "from Menu M\n" +
                "where M.storeIdx = ?";
        int getContentsParams = storeIdx;

        return this.jdbcTemplate.queryForObject(getContentsQuery,
                (rs, rowNum) -> new GetMenuInfoRes(
                        rs.getString("menuInfo"),
                        rs.getInt("foodTypeNum"),
                        rs.getString("foodOrigin")),
                getContentsParams);
    }

    // [GET] 가게 음식담기 조회 API
    public GetFoodInfoRes getFoodInfo(int foodIdx){
        String getContentsQuery = "select F.foodImgUrl, F.foodTxt, F.foodComponents, S.deliMinOrderPrice, F.foodPrice\n" +
                "from Store S, Food F\n" +
                "where S.idx = F.menuIdx and F.idx = ?";
        int getContentsParams = foodIdx;

        return this.jdbcTemplate.queryForObject(getContentsQuery,
                (rs, rowNum) -> new GetFoodInfoRes(
                        rs.getString("foodImgUrl"),
                        rs.getString("foodTxt"),
                        rs.getString("foodComponents"),
                        rs.getInt("deliMinOrderPrice"),
                        rs.getInt("foodPrice"),
                        getFoodFlavor(foodIdx)),
                getContentsParams);
    }

    // [GET] 음식 맛 조회 API (추가쿼리)
    public List<GetFoodFlavorRes> getFoodFlavor(int foodIdx){
        String getContentsQuery = "select FF.flavorIdx, FF.flavorTxt, Fl.flavorPrice\n" +
                "from Food F\n" +
                "join FoodFlavor FF on F.idx = FF.foodIdx\n" +
                "join Flavor Fl on FF.flavorIdx = Fl.idx\n" +
                "where F.idx = ?";
        int getContentsParams = foodIdx;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetFoodFlavorRes(
                        rs.getInt("flavorIdx"),
                        rs.getString("flavorTxt"),
                        rs.getInt("flavorPrice")),
                getContentsParams);
    }


}
