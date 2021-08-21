package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // [GET] 회원 1명 조회 API
    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select profileUrl, nickname, email, phoneNum, userRate\n" +
                "from User\n" +
                "where idx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("profileUrl"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("email"),
                        rs.getString("userRate")),
                getUserParams);
    }


    // [POST] 회원가입 API
    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (nickname, email, pwd, phoneNum, bitrhDate) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getNickname(), postUserReq.getEmail(), postUserReq.getPwd(), postUserReq.getPhoneNum(), postUserReq.getBirthDate()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }


    // [POST] 로그인 API
    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select idx, nickname, email, pwd, phoneNum\n" +
                "from User\n" +
                "where email = ?";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("idx"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("pwd"),
                        rs.getString("phoneNum")
                ),
                getPwdParams
                );

    }



}
