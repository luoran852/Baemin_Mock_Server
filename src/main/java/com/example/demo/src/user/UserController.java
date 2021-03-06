package com.example.demo.src.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

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
     * 휴대폰번호 인증, 이메일 중복체크 API
     * [POST] /users/phone-email
     * @return BaseResponse<result>
     */
    @ResponseBody
    @PostMapping("/phone-email") // (POST) 15.165.16.88:8000/users/phone-email
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

    /**
     * 네이버 소셜로그인 API
     * [POST] /users/naver-login
     */
    // 네이버 API 예제 - 회원프로필 조회
    @ResponseBody
    @PostMapping("/naver-login") // (GET) 15.165.16.88:8000/users/naver-login
    public BaseResponse<PostNaverLoginRes> naverLogIn(@RequestBody PostNaverLoginReq postNaverLoginReq) {
        try {
            if (postNaverLoginReq.getNaverToken() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_TOKEN);
            }
            String naverId = getNaverId(postNaverLoginReq.getNaverToken());
            PostNaverLoginRes postNaverLoginRes = userService.naverLogIn(naverId);
            return new BaseResponse<>(postNaverLoginRes);

        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    public String getNaverId (String naverToken) {
        String token = naverToken; // 네아로 접근 토큰 값";
        String header = "Bearer " + token; // Bearer 다음에 공백 추가
        String naverId = "";
        try {
            String apiURL = "https://openapi.naver.com/v1/nid/me";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", header);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            ObjectMapper objectMapper =new ObjectMapper();
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
                Map<String, String> apiJson = (Map<String, String>) objectMapper.readValue(inputLine, Map.class).get("response");
                System.out.println(apiJson.get("id"));
                naverId = apiJson.get("id");
            }
            br.close();
            System.out.println(response);


        } catch (Exception e) {
            System.out.println(e);
        }
        return naverId;
    }






}
