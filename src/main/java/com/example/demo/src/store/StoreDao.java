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

    // [GET] 가게리스트 메인메뉴 조회 API
    public List<GetMainMenuListRes> getMainMenu(int storeIdx){
        String getContentsQuery = "select F.foodTxt\n" +
                "from Store S\n" +
                "   join Food F on S.idx = F.menuIdx\n" +
                "         join FoodTypeFood FTF on F.idx = FTF.foodIdx\n" +
                "where S.idx = ?\n" +
                "group by F.idx\n" +
                "limit 2";
        int getContentsParams = storeIdx;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetMainMenuListRes(
                        rs.getString("foodTxt")),
                getContentsParams);
    }

    // [GET] 가게 리스트 조회 API
    public List<GetStoreListRes> getStoreLists(int type, int category, int sort) {
        String getContentsQuery = "";
        int getContentsParams1 = 0, getContentsParams2 = 0;

        // 기본순 가게리스트 정렬
        if (sort == 1) {
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

        // 배달 빠른 순 가게리스트 정렬
        if (sort == 3) {
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

    // [GET] 가게 정보 조회 API
    public GetStoreInfoRes getStoreInfo(int storeIdx){
        String getContentsQuery = "select storeName, foodPosterUrl, S.rating, reviewNum, bossCommentNum, keepNum, deliMinOrderPrice, deliPayType, deliveryTime, deliveryTip, packMinOrderPrice, howToUse, cookingTime, locationInfo, storeDistance, packPayType, storeInfoImgUrl, storeFullName, operatingTime, holiday, storePhoneNum, deliveryArea, guideAndBenefits, RepresentativeName, storeNum\n" +
                "from Store S\n" +
                "join (select R.storeIdx, count(storeIdx) reviewNum\n" +
                "from Review R\n" +
                "group by storeIdx) reviewNum on reviewNum.storeIdx = S.idx\n" +
                "join (select BC.storeIdx, count(storeIdx) bossCommentNum\n" +
                "from BossComment BC, Store S\n" +
                "group by storeIdx) bossCommentNum on S.idx = bossCommentNum.storeIdx\n" +
                "join (select K.storeIdx, count(storeIdx) keepNum\n" +
                "from Keep K, Store S\n" +
                "group by storeIdx) keepNum on S.idx = keepNum.storeIdx\n" +
                "where S.idx = ?";
        int getContentsParams = storeIdx;
        return this.jdbcTemplate.queryForObject(getContentsQuery,
                (rs, rowNum) -> new GetStoreInfoRes(
                        rs.getInt("profileUrl"),
                        rs.getInt("profileUrl"),
                        rs.getInt("profileUrl"),
                        rs.getInt("profileUrl"),
                        rs.getInt("profileUrl"),
                        rs.getInt("profileUrl"),
                        rs.getInt("profileUrl"),
                        rs.getInt("profileUrl"),
                        rs.getInt("profileUrl"),
                        rs.getFloat("profileUrl"),
                        rs.getString("profileUrl"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("email"),
                        rs.getString("userRate"),
                        rs.getString("profileUrl"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("email"),
                        rs.getString("userRate"),
                        rs.getString("profileUrl"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("email"),
                        rs.getString("userRate")),
                getContentsParams);
    }


}
