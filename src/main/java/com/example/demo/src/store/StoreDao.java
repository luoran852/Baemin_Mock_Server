package com.example.demo.src.store;

import com.example.demo.src.store.model.*;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserReq;
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
            getContentsQuery = "select distinct S.idx storeIdx, storePosterUrl, storeName, isPacking, isNew, isCoupon, S.rating, reviewNum, deliMinOrderPrice, deliveryTip, deliveryTime\n" +
                    "from Store S\n" +
                    "join (select R.storeIdx, count(storeIdx) reviewNum\n" +
                    "from Review R\n" +
                    "group by storeIdx) reviewNum on reviewNum.storeIdx = S.idx,\n" +
                    "     Category Ca join CategoryStore CS on Ca.idx = CS.categotyIdx,\n" +
                    "     Type T join TypeStore TS on T.idx = TS.typeIdx\n" +
                    "where T.idx = ? and Ca.idx = ?\n" +
                    "order by deliveryTime asc";

            getContentsParams1 = type;
            getContentsParams2 = category;
        }

        // 배달팁 낮은순 가게리스트 정렬
        if (sort == 2) {
            getContentsQuery = "select distinct S.idx storeIdx, storePosterUrl, storeName, isPacking, isNew, isCoupon, S.rating, reviewNum, deliMinOrderPrice, deliveryTip, deliveryTime\n" +
                    "from Store S\n" +
                    "join (select R.storeIdx, count(storeIdx) reviewNum\n" +
                    "from Review R\n" +
                    "group by storeIdx) reviewNum on reviewNum.storeIdx = S.idx,\n" +
                    "     Category Ca join CategoryStore CS on Ca.idx = CS.categotyIdx,\n" +
                    "     Type T join TypeStore TS on T.idx = TS.typeIdx\n" +
                    "where T.idx = ? and Ca.idx = ?\n" +
                    "order by deliveryTip asc";

            getContentsParams1 = type;
            getContentsParams2 = category;
        }

        // 기본순 가게리스트 정렬
        if (sort == 3) {
            getContentsQuery = "select distinct S.idx storeIdx, storePosterUrl, storeName, isPacking, isNew, isCoupon, S.rating, reviewNum, deliMinOrderPrice, deliveryTip, deliveryTime\n" +
                    "from Store S\n" +
                    "join (select R.storeIdx, count(storeIdx) reviewNum\n" +
                    "from Review R\n" +
                    "group by storeIdx) reviewNum on reviewNum.storeIdx = S.idx,\n" +
                    "     Category Ca join CategoryStore CS on Ca.idx = CS.categotyIdx,\n" +
                    "     Type T join TypeStore TS on T.idx = TS.typeIdx\n" +
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
        String getContentsQuery = "select distinct F.foodTxt\n" +
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
        String getContentsQuery = "select distinct storeName, foodPosterUrl, S.rating, reviewNum, bossCommentNum, keepNum, deliMinOrderPrice, deliPayType, deliveryTime, deliveryTip, packMinOrderPrice, howToUse, cookingTime, locationInfo, storeDistance, packPayType, storeInfo, storeInfoImgUrl, storeFullName, operatingTime, holiday, storePhoneNum, deliveryArea, guideAndBenefits, RepresentativeName, storeNum\n" +
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


    // [GET] 가게 메뉴공지 조회 API
    public GetStoreMenuRes getStoreMenu(int storeIdx){
        String getContentsQuery = "select distinct M.menuInfo, M.foodTypeNum, M.foodOrigin\n" +
                "from Menu M\n" +
                "where M.storeIdx = ?";
        int getContentsParams = storeIdx;

        return this.jdbcTemplate.queryForObject(getContentsQuery,
                (rs, rowNum) -> new GetStoreMenuRes(
                        rs.getString("menuInfo"),
                        rs.getInt("foodTypeNum"),
                        rs.getString("foodOrigin"),
                        getMenuInfo(storeIdx)),
                getContentsParams);
    }

    // [GET] 가게 메뉴 조회 API (추가쿼리)
    public List<GetMenuInfoRes> getMenuInfo(int storeIdx){
        String getContentsQuery = "select distinct F.idx foodIdx, F.foodTxt, FTF.foodTypeIdx, FTF.foodTypeTxt, F.foodComment, F.foodPrice, F.foodImgUrl, F.isPopular, F.isSoldOut, F.isAlcohol\n" +
                "from Food F\n" +
                "join FoodTypeFood FTF on F.idx = FTF.foodIdx\n" +
                "where F.menuIdx = ?\n" +
                "order by FTF.foodTypeIdx asc";
        int getContentsParams = storeIdx;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetMenuInfoRes(
                        rs.getInt("foodIdx"),
                        rs.getString("foodTxt"),
                        rs.getInt("foodTypeIdx"),
                        rs.getString("foodTypeTxt"),
                        rs.getString("foodComment"),
                        rs.getInt("foodPrice"),
                        rs.getString("foodImgUrl"),
                        rs.getInt("isPopular"),
                        rs.getInt("isSoldOut"),
                        rs.getInt("isAlcohol")),
                getContentsParams);
    }

    // [GET] 가게 음식담기 조회 API
    public GetFoodInfoRes getFoodInfo(int foodIdx){
        String getContentsQuery = "select distinct F.foodImgUrl, F.foodTxt, F.foodComponents, S.deliMinOrderPrice, F.foodPrice\n" +
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
        String getContentsQuery = "select distinct FF.flavorIdx, FF.flavorTxt, Fl.flavorPrice\n" +
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

    // [GET] 우리 동네 빠른 배달 조회 API
    public List<GetFastStoreListRes> getFastStoreLists(int type) {
        String getContentsQuery = "";

        // 홈
        if (type == 0) {
            getContentsQuery = "select distinct idx storeIdx, storePosterUrl, storeName, isPacking, isNew, isCoupon, rating, deliveryTip, deliveryTime\n" +
                    "from Store S\n" +
                    "order by rand()";
        }

        // 배달
        if (type == 1) {
            getContentsQuery = "select distinct S.idx storeIdx, storePosterUrl, storeName, isPacking, isNew, isCoupon, rating, deliveryTip, deliveryTime\n" +
                    "from Store S, Type T join TypeStore TS on T.idx = TS.idx\n" +
                    "where T.idx = 1\n" +
                    "order by rand()";
        }


        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetFastStoreListRes(
                        rs.getInt("storeIdx"),
                        rs.getString("storePosterUrl"),
                        rs.getString("storeName"),
                        rs.getInt("isPacking"),
                        rs.getInt("isNew"),
                        rs.getInt("isCoupon"),
                        rs.getFloat("rating"),
                        rs.getInt("deliveryTip"),
                        rs.getInt("deliveryTime"),
                        getCatMenuInfo(rs.getInt("storeIdx"))
                        )
        );
    }

    // [GET] 카테고리 & 메인메뉴 1개 (Plus 쿼리)
    public GetCatMenuInfoRes getCatMenuInfo(int storeIdx){
        String getContentsQuery = "select distinct Ca.categoryTxt, F.foodTxt\n" +
                "from Store S join Food F on F.menuIdx = S.idx,\n" +
                "     Category Ca join CategoryStore CS on Ca.idx = CS.categotyIdx\n" +
                "where CS.storeIdx = F.menuIdx and S.idx = ?\n" +
                "limit 1";
        int getContentsParams = storeIdx;

        return this.jdbcTemplate.queryForObject(getContentsQuery,
                (rs, rowNum) -> new GetCatMenuInfoRes(
                        rs.getString("categoryTxt"),
                        rs.getString("foodTxt")),
                getContentsParams);
    }

    // [GET] 배민1에 새로 들어왔어요 조회 API
    public List<GetNewStoreListRes> getNewStoreList() {

        String getContentsQuery = "select distinct S.idx storeIdx, storePosterUrl, storeName, isPacking, isNew, isCoupon, rating, deliveryTip, deliveryTime\n" +
                    "from Store S, Type T join TypeStore TS on T.idx = TS.idx\n" +
                    "where T.idx = 1\n" +
                    "order by rand()";


        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetNewStoreListRes(
                        rs.getInt("storeIdx"),
                        rs.getString("storePosterUrl"),
                        rs.getString("storeName"),
                        rs.getInt("isPacking"),
                        rs.getInt("isNew"),
                        rs.getInt("isCoupon"),
                        rs.getFloat("rating"),
                        rs.getInt("deliveryTip"),
                        rs.getInt("deliveryTime"))
        );
    }

    // [GET] 배민1 추천 조회 API
    public List<GetBaemin1StoreListRes> getBaemin1StoreList(int sort) {
        String getContentsQuery = "";

        // 배달 빠른순 (sort = 1)
        if (sort == 1) {
            getContentsQuery = "select distinct S.idx storeIdx, storeName, rating, deliveryTime, deliMinOrderPrice, storeDistance, deliveryTip, isNew, isCoupon\n" +
                    "from Store S, Type T join TypeStore TS on T.idx = TS.idx\n" +
                    "where T.idx = 2\n" +
                    "order by deliveryTime asc";
        }

        // 배달팁 낮은순 (sort = 2)
        if (sort == 2) {
            getContentsQuery = "select distinct S.idx storeIdx, storeName, rating, deliveryTime, deliMinOrderPrice, storeDistance, deliveryTip, isNew, isCoupon\n" +
                    "from Store S, Type T join TypeStore TS on T.idx = TS.idx\n" +
                    "where T.idx = 2\n" +
                    "order by deliveryTip asc";
        }

        // 기본순 (sort = 3)
        if (sort == 3) {
            getContentsQuery = "select distinct S.idx storeIdx, storeName, rating, deliveryTime, deliMinOrderPrice, storeDistance, deliveryTip, isNew, isCoupon\n" +
                    "from Store S, Type T join TypeStore TS on T.idx = TS.idx\n" +
                    "where T.idx = 2\n" +
                    "order by rand()";
        }


        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetBaemin1StoreListRes(
                        getBaeminOneImages(rs.getInt("storeIdx")),
                        rs.getInt("storeIdx"),
                        rs.getString("storeName"),
                        rs.getFloat("rating"),
                        rs.getInt("deliveryTime"),
                        rs.getInt("deliMinOrderPrice"),
                        rs.getFloat("storeDistance"),
                        rs.getInt("deliveryTip"),
                        rs.getInt("isNew"),
                        rs.getInt("isCoupon"))
                );
    }

    // [GET] 배민1 이미지 3개 조회 (Plus 쿼리)
    public List<String> getBaeminOneImages(int storeIdx){
        String getContentsQuery = "select distinct foodImgUrl\n" +
                "from Food F\n" +
                "where F.menuIdx = ?\n" +
                "limit 3";
        int getContentsParams = storeIdx;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) ->
                        rs.getString("foodImgUrl"),
                getContentsParams);
    }

    // [GET] 이럴 때 포장/방문 해보세요 조회 API
    public List<GetVisitStoreListRes> getVisitStoreList(int tag) {
        String getContentsQuery = "";

        // #영화 (tag = 1)
        if (tag == 1) {
            getContentsQuery = "select distinct S.idx storeIdx, storeName, foodPosterUrl, deliveryTime\n" +
                    "from Store S\n" +
                    "where S.idx = 1 or S.idx = 3 or S.idx = 4 or S.idx = 5\n" +
                    "order by rand()";
        }

        // #국물이 (tag = 2)
        if (tag == 2) {
            getContentsQuery = "select distinct S.idx storeIdx, storeName, foodPosterUrl, deliveryTime\n" +
                    "from Store S\n" +
                    "where S.idx = 1 or S.idx = 3 or S.idx = 4\n" +
                    "order by rand()";
        }


        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetVisitStoreListRes(
                        rs.getInt("storeIdx"),
                        rs.getString("storeName"),
                        rs.getString("foodPosterUrl"),
                        rs.getInt("deliveryTime"),
                        getVisitCategory(rs.getInt("storeIdx"))));
    }

    // [GET] 이럴 때 포장/방문 해보세요 조회 API (Plus 쿼리)
    public GetVisitCateRes getVisitCategory(int storeIdx){
        String getContentsQuery = "select distinct Ca.categoryTxt, F.foodTxt\n" +
                "from Store S join Food F on F.menuIdx = S.idx,\n" +
                "     Category Ca join CategoryStore CS on Ca.idx = CS.categotyIdx\n" +
                "where CS.storeIdx = F.menuIdx and S.idx = ?\n" +
                "limit 1";
        int getContentsParams = storeIdx;

        return this.jdbcTemplate.queryForObject(getContentsQuery,
                (rs, rowNum) -> new GetVisitCateRes(
                        rs.getString("categoryTxt"),
                        rs.getString("foodTxt")),
                getContentsParams);
    }

    // [GET] 가게 쿠폰 조회 API
    public List<GetStoreCouponListRes> getStoreCouponList(int storeIdx) {

        String getContentsQuery = "select distinct C.idx couponIdx, couponPrice, isDelivery, isPacking, minOrder, validity, isDownloaded\n" +
                "from Coupon C join CouponUser CU on C.idx = CU.couponIdx\n" +
                "where C.storeIdx = ?";
        int getContentsParams = storeIdx;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetStoreCouponListRes(
                        rs.getInt("couponIdx"),
                        rs.getInt("couponPrice"),
                        rs.getInt("isDelivery"),
                        rs.getInt("isPacking"),
                        rs.getInt("minOrder"),
                        rs.getInt("validity"),
                        rs.getInt("isDownloaded")),
                getContentsParams
        );
    }

    // [POST] 가게 리뷰 올리기 API
    public int postReview(PostReviewReq postReviewReq, int userIdxByJwt, int storeIdx){
        String createReviewQuery1 = "insert into ReviewFood (userIdx, storeIdx, foodTxt1, foodTxt2, foodTxt3) values (?, ?, ?, ifnull(?, null), ifnull(?, null))";
        Object[] createUserParams1 = new Object[]{userIdxByJwt, storeIdx, postReviewReq.getFoodTxt1(), postReviewReq.getFoodTxt2(), postReviewReq.getFoodTxt3()};
        this.jdbcTemplate.update(createReviewQuery1, createUserParams1);

        String createReviewQuery2 = "insert into Review (storeIdx, userIdx, nickName, rating, reviewTxt, reviewImgUrl) values (?, ?, ?, ?, ifnull(?, null), ifnull(?, null))";
        Object[] createUserParams2 = new Object[]{storeIdx, userIdxByJwt, postReviewReq.getNickName(), postReviewReq.getRating(), postReviewReq.getReviewTxt(), postReviewReq.getReviewImgUrl()};
        this.jdbcTemplate.update(createReviewQuery2, createUserParams2);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    // [POST] 가게 사장님 댓글 올리기 API
    public int postBossComment(PostBossCommentReq postBossCommentReq, int userIdxByJwt, int storeIdx, int reviewIdx){
        String createContentsQuery1 = "insert into BossComment (storeIdx, reviewIdx, userIdx, bossCommentTxt) values (?, ?, ?, ?)";
        Object[] createContentsParams1 = new Object[]{storeIdx, reviewIdx, userIdxByJwt, postBossCommentReq.getBossCommentTxt()};

        return this.jdbcTemplate.update(createContentsQuery1,createContentsParams1);
    }

    // [GET] 가게 리뷰 정보 조회 API
    public GetReviewRes getReview(int storeIdx){
        String getContentsQuery = "select distinct date_format(BN.createdAt, '%Y년 %c월 %e일') as bossNoticeDate, bossNoticeImgUrl, bossNoticeTxt, round(sum(rating) / count(R.idx), 1) as TotalRating,\n" +
                "                (select count(R.idx) from Review R where R.rating = 5 and R.storeIdx = ?) as fiveRatingNum,\n" +
                "                (select count(R.idx) from Review R where R.rating = 4 and R.storeIdx = ?) as fourRatingNum,\n" +
                "                (select count(R.idx) from Review R where R.rating = 3 and R.storeIdx = ?) as threeRatingNum,\n" +
                "                (select count(R.idx) from Review R where R.rating = 2 and R.storeIdx = ?) as twoRatingNum,\n" +
                "                (select count(R.idx) from Review R where R.rating = 1 and R.storeIdx = ?) as oneRatingNum,\n" +
                "                bossWordTxt, date_format(BN.createdAt, '%Y년 %c월 %e일') as bossWordDate\n" +
                "from BossNotice BN join Review R on BN.storeIdx = R.storeIdx\n" +
                "where BN.storeIdx = ?";
        int getContentsParams1 = storeIdx;
        int getContentsParams2 = storeIdx;
        int getContentsParams3 = storeIdx;
        int getContentsParams4 = storeIdx;
        int getContentsParams5 = storeIdx;
        int getContentsParams6 = storeIdx;

        return this.jdbcTemplate.queryForObject(getContentsQuery,
                (rs, rowNum) -> new GetReviewRes(
                        rs.getString("bossNoticeDate"),
                        rs.getString("bossNoticeImgUrl"),
                        rs.getString("bossNoticeTxt"),
                        rs.getDouble("TotalRating"),
                        rs.getInt("fiveRatingNum"),
                        rs.getInt("fourRatingNum"),
                        rs.getInt("threeRatingNum"),
                        rs.getInt("twoRatingNum"),
                        rs.getInt("oneRatingNum"),
                        rs.getString("bossWordTxt"),
                        rs.getString("bossWordDate"),
                        reviewNum(storeIdx)),
                getContentsParams1, getContentsParams2, getContentsParams3, getContentsParams4, getContentsParams5, getContentsParams6);
    }

    // [GET] 가게 리뷰 조회 API (리뷰개수)- (추가 쿼리)
    public GetReviewNumRes reviewNum(int storeIdx){
        String getContentsQuery = "select distinct count(R.idx) as recentReviewNum, bossCommentNum\n" +
                "from Review R\n" +
                "join (select count(BC.idx) as bossCommentNum, userIdx\n" +
                "        from BossComment BC where storeIdx = ?) as bossCommentNum\n" +
                "where storeIdx = ?";
        int getContentsParams1 = storeIdx;
        int getContentsParams2 = storeIdx;

        return this.jdbcTemplate.queryForObject(getContentsQuery,
                (rs, rowNum) -> new GetReviewNumRes(
                        rs.getInt("recentReviewNum"),
                        rs.getInt("bossCommentNum")),
                getContentsParams1, getContentsParams2);
    }

    // [GET] 사용자 리뷰 조회
    public List<GetUserReviewListRes> getUserReviewList(int storeIdx, int sort) {

        String getContentsQuery = "";

        // 최신순 (sort = 1)
        if (sort == 1) {
            getContentsQuery = "select distinct R.idx reviewIdx, R.userIdx, R.nickName, profileUrl, rating, reviewImgUrl, reviewTxt, case\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 1\n" +
                    "        then '오늘'\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 2\n" +
                    "        then '어제'\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 3\n" +
                    "        then '그제'\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 7\n" +
                    "        then '이번주'\n" +
                    "    when timestampdiff(month , createdAt, current_timestamp()) < 1\n" +
                    "        then '한달전'\n" +
                    "    else\n" +
                    "        date_format(createdAt, '%Y년-%m월-%d일') end as createdAt\n" +
                    "from Review R join (select idx userIdx, nickName, profileUrl from User U) as User on R.userIdx = User.userIdx\n" +
                    "where storeIdx = ?\n" +
                    "order by createdAt asc";
        }

        // 별점높은순 (sort = 2)
        if (sort == 2) {
            getContentsQuery = "select distinct R.idx reviewIdx, R.userIdx, R.nickName, profileUrl, rating, reviewImgUrl, reviewTxt, case\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 1\n" +
                    "        then '오늘'\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 2\n" +
                    "        then '어제'\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 3\n" +
                    "        then '그제'\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 7\n" +
                    "        then '이번주'\n" +
                    "    when timestampdiff(month , createdAt, current_timestamp()) < 1\n" +
                    "        then '한달전'\n" +
                    "    else\n" +
                    "        date_format(createdAt, '%Y년-%m월-%d일') end as createdAt\n" +
                    "from Review R join (select idx userIdx, nickName, profileUrl from User U) as User on R.userIdx = User.userIdx\n" +
                    "where storeIdx = ?\n" +
                    "order by rating desc";
        }

        // 별점낮은순 (sort = 3)
        if (sort == 3) {
            getContentsQuery = "select distinct R.idx reviewIdx, R.userIdx, R.nickName, profileUrl, rating, reviewImgUrl, reviewTxt, case\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 1\n" +
                    "        then '오늘'\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 2\n" +
                    "        then '어제'\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 3\n" +
                    "        then '그제'\n" +
                    "    when timestampdiff(day, createdAt, current_timestamp()) < 7\n" +
                    "        then '이번주'\n" +
                    "    when timestampdiff(month , createdAt, current_timestamp()) < 1\n" +
                    "        then '한달전'\n" +
                    "    else\n" +
                    "        date_format(createdAt, '%Y년-%m월-%d일') end as createdAt\n" +
                    "from Review R join (select idx userIdx, nickName, profileUrl from User U) as User on R.userIdx = User.userIdx\n" +
                    "where storeIdx = ?\n" +
                    "order by rating asc";
        }

        int getContentsParams = storeIdx;
        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetUserReviewListRes(
                        rs.getInt("reviewIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("profileUrl"),
                        rs.getFloat("rating"),
                        rs.getString("reviewImgUrl"),
                        rs.getString("reviewTxt"),
                        rs.getString("createdAt"),
                        getReviewFood(rs.getInt("userIdx"), getContentsParams)),
                getContentsParams
        );
    }

    // [GET] 사용자 리뷰 조회 (음식 선택 - 추가 쿼리)
    public GetReviewFoodRes getReviewFood(int userIdx, int storeIdx){
        String getContentsQuery = "select distinct foodTxt1, foodTxt2, foodTxt3\n" +
                "from ReviewFood\n" +
                "where userIdx =? and storeIdx = ?\n" +
                "limit 3";
        int getContentsParams1 = userIdx;
        int getContentsParams2 = storeIdx;

        return this.jdbcTemplate.queryForObject(getContentsQuery,
                (rs, rowNum) -> new GetReviewFoodRes(
                        rs.getString("foodTxt1"),
                        rs.getString("foodTxt2"),
                        rs.getString("foodTxt3")),
                getContentsParams1, getContentsParams2);
    }

    // [GET] 주문하기 페이지 조회 API
    public GetOrderPageRes getOrderPage(int userIdx){
        String getContentsQuery = "select distinct address, phoneNum, BP.payMoney, P.pointSavePrice\n" +
                "from User U\n" +
                "join BaeminPay BP on U.idx = BP.userIdx\n" +
                "join Point P on U.idx = P.userIdx\n" +
                "where U.idx = ?";
        int getContentsParams1 = userIdx;

        return this.jdbcTemplate.queryForObject(getContentsQuery,
                (rs, rowNum) -> new GetOrderPageRes(
                        rs.getString("address"),
                        rs.getString("phoneNum"),
                        rs.getInt("payMoney"),
                        rs.getInt("pointSavePrice"),
                        getOrderingCouponList(userIdx)),
                getContentsParams1);
    }

    // [GET] 주문하기 페이지 조회 API (추가쿼리 - 쿠폰 조회)
    public List<GetOrderingCouponListRes> getOrderingCouponList(int userIdx) {

        String getContentsQuery = "select distinct C.idx couponIdx, C.couponPrice, C.storeTxt, timestampdiff(day, current_timestamp, date_add(C.createdAt, interval 30 day)) as leftDate,\n" +
                "                date_format(date_add(C.createdAt, interval 30 day), '%Y/%m/%d') as expireDate\n" +
                "from Store S join Coupon C on S.idx = C.storeIdx\n" +
                "    join CouponUser CU on C.idx = CU.couponIdx\n" +
                "    join User U on CU.userIdx = U.idx\n" +
                "where U.idx = ?";
        int getContentsParams = userIdx;

        return this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new GetOrderingCouponListRes(
                        rs.getInt("couponIdx"),
                        rs.getInt("couponPrice"),
                        rs.getString("storeTxt"),
                        rs.getInt("leftDate"),
                        rs.getString("expireDate")),
                getContentsParams
        );
    }

    // [POST] 주문하기 API (메인 쿼리 - 돈 계산)
    public int postOrderCalc(PostOrderReq postOrderReq, int userIdxByJwt, int storeIdx){

        String createContentsQuery1 = "insert into\n" +
                "    Ordering (userIdx, storeIdx, totalPrice, requestToStore, requestToRider, needSpoon, payMethodIdx, payMethodTxt)\n" +
                "    values (?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] createContentsParams1 = new Object[]{userIdxByJwt, storeIdx, postOrderReq.getTotalPrice(),
                postOrderReq.getRequestToStore(), postOrderReq.getRequestToRider(), postOrderReq.getNeedSpoon(),
                postOrderReq.getPayMethodIdx(), postOrderReq.getPayMethodTxt()};
        this.jdbcTemplate.update(createContentsQuery1, createContentsParams1);

        String createContentsQuery2 = "update BaeminPay BP\n" +
                "    join Point P on BP.userIdx = P.userIdx\n" +
                "    join CouponUser CU on P.userIdx = CU.userIdx\n" +
                "set BP.payMoney = ?, P.pointUsePrice = ?,\n" +
                "    CU.isDeleted = case when ? = 0 then 0 else 1 end where BP.userIdx = ?";

        Object[] createContentsParams2 = new Object[]{postOrderReq.getUsedPayMoney(), postOrderReq.getUsedPoint(),
                postOrderReq.getUsedCouponIdx(), userIdxByJwt};
        this.jdbcTemplate.update(createContentsQuery2, createContentsParams2);

        String lastInserIdQuery = "select idx\n" +
                "from Ordering\n" +
                "where userIdx = ? and storeIdx = ?\n" +
                "order by idx desc\n" +
                "limit 1";

        int getContentsParams1 = userIdxByJwt;
        int getContentsParams2 = storeIdx;

        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class, getContentsParams1, getContentsParams2);

    }

    // [POST] 주문하기 API (추가 쿼리)
    public String saveOrderInfo(int orderIdx, PostOrderReq postOrderReq, int userIdxByJwt, int storeIdx){

        String resultMsg = "주문하기 성공";
        // 음식정보 담기
        for (int i = 0; i < postOrderReq.getTotalFoodNum(); i++) {
            String createContentsQuery1 = "update OrderFood\n" +
                    "set orderIdx = ?, foodIdx = ?, flavorIdx = ?";
            Object[] createContentsParams1 = new Object[]{orderIdx, postOrderReq.getFoodList().get(i).getFoodIdx(),
                    postOrderReq.getFoodList().get(i).getFlavorIdx()};
            this.jdbcTemplate.update(createContentsQuery1, createContentsParams1);
        }


        String createContentsQuery2 = "update OrderHistory\n" +
                "set userIdx = ?, storeIdx = ?, orderIdx = ?, orderNumber = LEFT(MD5(RAND()), 10)";
        Object[] createContentsParams2 = new Object[]{userIdxByJwt, storeIdx, orderIdx};
        this.jdbcTemplate.update(createContentsQuery2, createContentsParams2);

        return resultMsg;
    }




}
