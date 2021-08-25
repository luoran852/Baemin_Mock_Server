package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.*;
import com.example.demo.src.user.model.PostEmailCheckRes;
import com.example.demo.src.user.model.PostLoginReq;
import com.example.demo.src.user.model.PostLoginRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/stores")
public class StoreController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StoreProvider storeProvider;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final JwtService jwtService;


    public StoreController(StoreProvider storeProvider, StoreService storeService, JwtService jwtService){
        this.storeProvider = storeProvider;
        this.storeService = storeService;
        this.jwtService = jwtService;
    }


    /**
     * 가게 리스트 조회 API
     * [GET] /stores/list?type=?&category=?&sort=?
     * @return BaseResponse<List<GetStoreListRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/list") // (GET) 15.165.16.88:8000/stores/list?type=?&category=?&sort=?
    public BaseResponse<List<GetStoreListRes>> getStoreLists(@RequestParam int type, @RequestParam int category, @RequestParam int sort) {
        try{
            // type error message
            if (type < 1 || type > 8) {
                return new BaseResponse<>(GET_STORES_TYPE_ERROR);
            }
            // category error message
            if (category < 1 || category > 20) {
                return new BaseResponse<>(GET_STORES_CATEGORY_ERROR);
            }
            // sort error message
            if (sort < 1 || sort > 3) {
                return new BaseResponse<>(GET_STORES_SORT_ERROR);
            }
            // Get store lists
            List<GetStoreListRes> getStoreListRes = storeProvider.getStoreLists(type, category, sort);
            return new BaseResponse<>(getStoreListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 정보 조회 API
     * [GET] /stores/:storeIdx/info
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{storeIdx}/info") // (GET) 15.165.16.88:8000/stores/:storeIdx/info
    public BaseResponse<GetStoreInfoRes> getStoreInfo(@PathVariable("storeIdx") int storeIdx) {
        // Get Store Info
        try{
            // storeIdx error message
            if (storeIdx < 1 || storeIdx > 6) {
                return new BaseResponse<>(GET_STORES_STOREIDX_ERROR);
            }
            GetStoreInfoRes getStoreInfoRes = storeProvider.getStoreInfo(storeIdx);
            return new BaseResponse<>(getStoreInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 메뉴 조회 API
     * [GET] /stores/menu/:storeIdx
     * @return BaseResponse<getStoreMenuRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/menu/{storeIdx}") // (GET) 15.165.16.88:8000/stores/menu/:storeIdx
    public BaseResponse<GetStoreMenuRes> getStoreMenu(@PathVariable("storeIdx") int storeIdx) {
        try{
            // Get store menu
            GetStoreMenuRes getStoreMenuRes = storeProvider.getStoreMenu(storeIdx);
            return new BaseResponse<>(getStoreMenuRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 음식담기 조회 API
     * [GET] /stores/food/:foodIdx
     * @return BaseResponse<GetFoodInfoRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/food/{foodIdx}") // (GET) 15.165.16.88:8000/stores/food/:foodIdx
    public BaseResponse<GetFoodInfoRes> getFoodInfo(@PathVariable("foodIdx") int foodIdx) {
        // Get Food Info
        try{
            GetFoodInfoRes getFoodInfoRes = storeProvider.getFoodInfo(foodIdx);
            return new BaseResponse<>(getFoodInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 우리 동네 빠른 배달 조회 API
     * [GET] /stores/fast-delivery?type=1
     * @return BaseResponse<List<GetFastStoreListRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/fast-delivery") // (GET) 15.165.16.88:8000/stores/fast-delivery?type=1
    public BaseResponse<List<GetFastStoreListRes>> getFastStoreLists(@RequestParam int type) {
        try{
            // type error message
            if (type < 0 || type > 1) {
                return new BaseResponse<>(GET_FAST_STORES_TYPE_ERROR);
            }
            // Get store lists
            List<GetFastStoreListRes> getFastStoreListRes = storeProvider.getFastStoreLists(type);
            return new BaseResponse<>(getFastStoreListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 배민1에 새로 들어왔어요 조회 API
     * [GET] /stores/baemin-one/new
     * @return BaseResponse<List<GetNewStoreListRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/baemin-one/new") // (GET) 15.165.16.88:8000/stores/baemin-one/new
    public BaseResponse<List<GetNewStoreListRes>> getNewStoreList() {
        try{
            // Get store lists
            List<GetNewStoreListRes> getNewStoreListRes = storeProvider.getNewStoreList();
            return new BaseResponse<>(getNewStoreListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 배민1 추천 조회 API
     * [GET] /stores/baemin-one/list?sort=1
     * @return BaseResponse<List<GetBaemin1StoreListRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/baemin-one/list") // (GET) 15.165.16.88:8000/stores/baemin-one/list?sort=1
    public BaseResponse<List<GetBaemin1StoreListRes>> getBaemin1StoreList(@RequestParam int sort) {
        try{
            // sort error message
            if (sort < 1 || sort > 3) {
                return new BaseResponse<>(GET_STORES_SORT_ERROR);
            }
            // Get store lists
            List<GetBaemin1StoreListRes> getBaemin1StoreListRes = storeProvider.getBaemin1StoreList(sort);
            return new BaseResponse<>(getBaemin1StoreListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 이럴 때 포장/방문 해보세요 조회 API
     * [GET] /stores/visit?tag=1
     * @return BaseResponse<List<GetVisitStoreListRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/visit") // (GET) 15.165.16.88:8000/stores/visit?tag=1
    public BaseResponse<List<GetVisitStoreListRes>> getVisitStoreList(@RequestParam int tag) {
        try{
            // sort error message
            if (tag < 1 || tag > 2) {
                return new BaseResponse<>(GET_STORES_TAG_ERROR);
            }

            // Get store lists
            List<GetVisitStoreListRes> getVisitStoreListRes = storeProvider.getVisitStoreList(tag);
            return new BaseResponse<>(getVisitStoreListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 쿠폰 조회 API
     * [GET] /stores/:storeIdx/coupons
     * @return BaseResponse<List<GetStoreCouponListRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/{storeIdx}/coupons") // (GET) 15.165.16.88:8000/stores/:storeIdx/coupons
    public BaseResponse<List<GetStoreCouponListRes>> getStoreCouponList(@PathVariable("storeIdx") int storeIdx) {
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // storeIdx error message
            if (storeIdx < 1 || storeIdx > 6) {
                return new BaseResponse<>(GET_STORES_STOREIDX_ERROR);
            }

            // Get Coupon lists
            List<GetStoreCouponListRes> getStoreCouponListRes = storeProvider.getStoreCouponList(storeIdx);
            return new BaseResponse<>(getStoreCouponListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 리뷰 올리기 API
     * [POST] /stores/:storeIdx/review
     * @return BaseResponse<PostReviewRes>
     */
    @ResponseBody
    @PostMapping("/{storeIdx}/review") // (POST) 15.165.16.88:8000/stores/:storeIdx/review
    public BaseResponse<PostReviewRes> postReview(@RequestBody PostReviewReq postReviewReq, @PathVariable("storeIdx") int storeIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            PostReviewRes postReviewRes = storeService.postReview(postReviewReq, userIdxByJwt, storeIdx);
            return new BaseResponse<>(postReviewRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 가게 사장님 댓글 올리기 API
     * [POST] /stores/:storeIdx/boss-comment/:reviewIdx
     * @return BaseResponse<PostBossCommentRes>
     */
    @ResponseBody
    @PostMapping("/{storeIdx}/boss-comment/{reviewIdx}") // (POST) 15.165.16.88:8000/stores/:storeIdx/boss-comment/:reviewIdx
    public BaseResponse<PostBossCommentRes> postBossComment(@RequestBody PostBossCommentReq postBossCommentReq, @PathVariable("storeIdx") int storeIdx, @PathVariable("reviewIdx") int reviewIdx){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //jwt와 userIdx가 같은 유저인지 확인
            if (postBossCommentReq.getUserIdx() != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostBossCommentRes postBossCommentRes = storeService.postBossComment(postBossCommentReq, userIdxByJwt, storeIdx, reviewIdx);

            return new BaseResponse<>(postBossCommentRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 가게 리뷰 정보 조회 API
     * [GET] /stores/:storeIdx/review
     * @return BaseResponse<GetReviewRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{storeIdx}/review") // (GET) 15.165.16.88:8000/stores/:storeIdx/review
    public BaseResponse<GetReviewRes> getReview(@PathVariable("storeIdx") int storeIdx) {
        // Get Food Info
        try{
            GetReviewRes getReviewRes = storeProvider.getReview(storeIdx);
            return new BaseResponse<>(getReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 사용자 리뷰 조회 API
     * [GET] /stores/:storeIdx/user-review?sort=1
     * @return BaseResponse<List<GetUserReviewListRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/{storeIdx}/user-review") // (GET) 15.165.16.88:8000/stores/:storeIdx/user-review?sort=1
    public BaseResponse<List<GetUserReviewListRes>> getUserReviewList(@PathVariable("storeIdx") int storeIdx, @RequestParam int sort) {
        try{
            // sort error message
            if (sort < 1 || sort > 3) {
                return new BaseResponse<>(GET_STORES_SORT_ERROR);
            }

            // Get User Review lists
            List<GetUserReviewListRes> getUserReviewListRes = storeProvider.getUserReviewList(storeIdx, sort);
            return new BaseResponse<>(getUserReviewListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }





}