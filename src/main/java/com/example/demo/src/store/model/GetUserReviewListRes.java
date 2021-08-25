package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserReviewListRes {
    private int reviewIdx;
    private int userIdx;
    private String nickName;
    private String profileUrl;
    private float rating;
    private String reviewImgUrl;
    private String reviewTxt;
    private String createdAt;
    private GetReviewFoodRes reviewFoodList;
}
