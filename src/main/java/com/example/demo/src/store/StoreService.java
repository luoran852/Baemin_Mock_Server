package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.store.model.*;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class StoreService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StoreDao storeDao;
    private final StoreProvider storeProvider;
    private final JwtService jwtService;


    @Autowired
    public StoreService(StoreDao storeDao, StoreProvider storeProvider, JwtService jwtService) {
        this.storeDao = storeDao;
        this.storeProvider = storeProvider;
        this.jwtService = jwtService;

    }



    // [POST] 가게 리뷰 올리기 API
    public PostReviewRes postReview(PostReviewReq postReviewReq, int userIdxByJwt, int storeIdx) throws BaseException {

        try{
            int reviewIdx = storeDao.postReview(postReviewReq, userIdxByJwt, storeIdx);
            return new PostReviewRes(reviewIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 가게 사장님 댓글 올리기 API
    public PostBossCommentRes postBossComment(PostBossCommentReq postBossCommentReq, int userIdxByJwt, int storeIdx, int reviewIdx) throws BaseException {

        try{
            storeDao.postBossComment(postBossCommentReq, userIdxByJwt, storeIdx, reviewIdx);
            String result = "가게 사장님 댓글 올리기 성공";
            return new PostBossCommentRes(result);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
