package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int idx;
    private String nickname;
    private String email;
    private String pwd;
    private String phoneNum;
    private String naverId;
}
