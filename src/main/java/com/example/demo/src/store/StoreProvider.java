package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.store.model.*;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.AES128;
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
    public List<GetStoreMenuRes> getStoreMenu(int storeIdx) throws BaseException{
        try{
            List<GetStoreMenuRes> getStoreMenuRes = storeDao.getStoreMenu(storeIdx);
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


}
