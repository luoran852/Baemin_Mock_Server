package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private String profileUrl;
    private String nickname;
    private String email;
    private String phoneNum;
    private String userRate;
}
