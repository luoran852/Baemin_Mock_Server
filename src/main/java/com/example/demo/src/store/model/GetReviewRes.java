package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class GetReviewRes {
    private String bossNoticeDate;
    private String bossNoticeImgUrl;
    private String bossNoticeTxt;
    private double TotalRating;
    private int fiveRatingNum;
    private int fourRatingNum;
    private int threeRatingNum;
    private int twoRatingNum;
    private int oneRatingNum;
    private String bossWordTxt;
    private String bossWordDate;
    private GetReviewNumRes reviewNum;
}
