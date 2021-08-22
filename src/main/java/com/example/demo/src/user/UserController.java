package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입 API
     * [POST] /users/sign-up
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/sign-up") // (POST) 15.165.16.88:8000/users/sign-up
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!

        // 이메일 빈값 체크
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //비밀번호 정규표현 (최소 8 자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자)
        if(!isRegexPwd(postUserReq.getPwd())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
        //휴대폰 번호 정규표현
        if(!isRegexPhoneNum(postUserReq.getPhoneNum())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }

        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 이메일 중복체크 API
     * [POST] /users/email
     * @return BaseResponse<result>
     */
    @ResponseBody
    @PostMapping("/email") // (POST) 15.165.16.88:8000/users/email
    public BaseResponse<PostEmailCheckRes> emailCheck(@RequestBody PostEmailCheckReq postEmailCheckReq){
        // 이메일 빈값 체크
        if(postEmailCheckReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //휴대폰 번호 정규표현
        if(!isRegexPhoneNum(postEmailCheckReq.getPhoneNum())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }

        try{
            PostEmailCheckRes postEmailCheckRes = userService.emailCheck(postEmailCheckReq);
            return new BaseResponse<>(postEmailCheckRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로그인 API
     * [POST] /users/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login") // (POST) 15.165.16.88:8000/users/login
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.

            // 이메일, 비번 빈값 체크
            if (postLoginReq.getEmail().equals("")){
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }
            if (postLoginReq.getPwd().equals("")){
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }

            PostLoginRes postLoginRes = userService.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 회원 1명 조회 API
     * [GET] /users/my-page/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/my-page/{userIdx}") // (GET) 15.165.16.88:8000/users/my-page/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }






}
