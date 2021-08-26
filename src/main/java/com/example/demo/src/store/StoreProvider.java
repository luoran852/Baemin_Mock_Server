package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class StoreProvider {

    private final StoreDao storeDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StoreProvider(StoreDao storeDao, JwtService jwtService) {
        this.storeDao = storeDao;
        this.jwtService = jwtService;
    }


    // [GET] 가게 리스트 조회 API
    public List<GetStoreListRes> getStoreLists(int type, int category, int sort) throws BaseException{
        try{
            List<GetStoreListRes> getStoreListRes = storeDao.getStoreLists(type, category, sort);
            return getStoreListRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 가게 정보 조회 API
    public GetStoreInfoRes getStoreInfo(int storeIdx) throws BaseException {
        try {
            GetStoreInfoRes getStoreInfoRes = storeDao.getStoreInfo(storeIdx);
            return getStoreInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 가게 메뉴 조회 API
    public GetStoreMenuRes getStoreMenu(int storeIdx) throws BaseException{
        try{
            GetStoreMenuRes getStoreMenuRes = storeDao.getStoreMenu(storeIdx);
            return getStoreMenuRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 가게 음식담기 조회 API
    public GetFoodInfoRes getFoodInfo(int foodIdx) throws BaseException {
        try {
            GetFoodInfoRes getFoodInfoRes = storeDao.getFoodInfo(foodIdx);
            return getFoodInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 우리 동네 빠른 배달 조회 API
    public List<GetFastStoreListRes> getFastStoreLists(int type) throws BaseException{
        try{
            List<GetFastStoreListRes> getFastStoreListRes = storeDao.getFastStoreLists(type);
            return getFastStoreListRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 배민1에 새로 들어왔어요 조회 API
    public List<GetNewStoreListRes> getNewStoreList() throws BaseException{
        try{
            List<GetNewStoreListRes> getNewStoreListRes = storeDao.getNewStoreList();
            return getNewStoreListRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 배민1 추천 조회 API
    public List<GetBaemin1StoreListRes> getBaemin1StoreList(int sort) throws BaseException{
        try{
            List<GetBaemin1StoreListRes> getBaemin1StoreListRes = storeDao.getBaemin1StoreList(sort);
            return getBaemin1StoreListRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 이럴 때 포장/방문 해보세요 조회 API
    public List<GetVisitStoreListRes> getVisitStoreList(int tag) throws BaseException{
        try{
            List<GetVisitStoreListRes> getVisitStoreListRes = storeDao.getVisitStoreList(tag);
            return getVisitStoreListRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 가게 쿠폰 조회 API
    public List<GetStoreCouponListRes> getStoreCouponList(int storeIdx) throws BaseException{
        try{
            List<GetStoreCouponListRes> getStoreCouponListRes = storeDao.getStoreCouponList(storeIdx);
            return getStoreCouponListRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 가게 리뷰 정보 조회 API
    public GetReviewRes getReview(int storeIdx) throws BaseException{
        try{
            GetReviewRes getReviewRes = storeDao.getReview(storeIdx);
            return getReviewRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 가게 사용자 리뷰 조회 API
    public List<GetUserReviewListRes> getUserReviewList(int storeIdx, int sort) throws BaseException{
        try{
            List<GetUserReviewListRes> getUserReviewListRes = storeDao.getUserReviewList(storeIdx, sort);
            return getUserReviewListRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 주문하기 페이지 조회 API
    public GetOrderPageRes getOrderPage(int userIdxByJwt) throws BaseException{
        try{
            GetOrderPageRes getOrderPageRes = storeDao.getOrderPage(userIdxByJwt);
            return getOrderPageRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 주문상태 확인 조회 API
    public GetOrderCheckRes getOrderCheckPage(int orderIdx) throws BaseException{
        try{
            GetOrderCheckRes getOrderCheckRes = storeDao.getOrderCheckPage(orderIdx);
            return getOrderCheckRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 최근에 주문했어요 API
    public List<GetStoreRecentListRes> getStoreRecentList(int userIdx) throws BaseException{
        try{
            List<GetStoreRecentListRes> getStoreRecentListRes = storeDao.getStoreRecentList(userIdx);
            return getStoreRecentListRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [GET] 찜 조회 API
    public List<GetKeepListRes> getKeepList(int userIdx) throws BaseException{
        try{
            List<GetKeepListRes> getKeepListRes = storeDao.getKeepList(userIdx);
            return getKeepListRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
