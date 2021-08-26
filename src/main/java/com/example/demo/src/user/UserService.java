package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    // [POST] 회원가입 API
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // 이메일 중복 체크
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPwd());
            postUserReq.setPwd(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [POST] 휴대폰번호 인증, 이메일 중복체크 API
    public PostEmailCheckRes emailCheck(PostEmailCheckReq postEmailCheckReq) throws BaseException {
        // 이메일 중복 체크
        if(userProvider.checkEmail(postEmailCheckReq.getEmail()) == 1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        try{
            String result = "휴대폰 번호, 이메일 인증 성공";
            return new PostEmailCheckRes(result);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // [POST] 로그인 API
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        User user = userDao.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPwd());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(postLoginReq.getPwd().equals(password)){
            int userIdx = userDao.getPwd(postLoginReq).getIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    // [POST] 네이버 소셜로그인 API
    public PostNaverLoginRes naverLogIn(String naverId) throws BaseException {
        try{
            // 유저 존재 여부 체크
            if(userProvider.checkNaver(naverId) == 1) {
                int userIdx = userDao.getNaverId(naverId).getIdx();
                String jwt = jwtService.createJwt(userIdx);
                return new PostNaverLoginRes(userIdx,jwt);
            }
            // 유저 존재하지 않으면 네이버용 회원가입
            else{
                int userIdx = userDao.naverLogIn(naverId);
                String jwt = jwtService.createJwt(userIdx);
                return new PostNaverLoginRes(userIdx,jwt);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

}
