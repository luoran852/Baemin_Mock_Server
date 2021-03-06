package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewReq {
    private String nickName;
    private float rating;
    private String reviewTxt;
    private String reviewImgUrl;
    private String foodTxt1;
    private String foodTxt2;
    private String foodTxt3;
}
