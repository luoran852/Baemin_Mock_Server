package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.*;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.example.demo.utils.ValidationRegex.*;


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
     * @return BaseResponse<List<GetStoreListRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/menu/{storeIdx}") // (GET) 15.165.16.88:8000/stores/menu/:storeIdx
    public BaseResponse<List<GetStoreMenuRes>> getStoreMenu(@PathVariable("storeIdx") int storeIdx) {
        try{
            // Get store menu
            List<GetStoreMenuRes> getStoreMenuRes = storeProvider.getStoreMenu(storeIdx);
            return new BaseResponse<>(getStoreMenuRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 음식담기 조회 API
     * [GET] /stores/food/:foodIdx
     * @return BaseResponse<GetUserRes>
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





}