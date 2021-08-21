package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetStoreInfoRes {
    private String storeName;
    private String foodPosterUrl;
    private float rating;
    private int reviewNum;
    private int bossCommentNum;
    private int keepNum;
    private int deliMinOrderPrice;
    private String deliPayType;
    private int deliveryTime;
    private int deliveryTip;
    private int packMinOrderPrice;
    private String howToUse;
    private int cookingTime;
    private String locationInfo;
    private float storeDistance;
    private String packPayType;
    private String storeInfo;
    private String storeInfoImgUrl;
    private String storeFullName;
    private String operatingTime;
    private String holiday;
    private String storePhoneNum;
    private String deliveryArea;
    private String guideAndBenefits;
    private String RepresentativeName;
    private String storeNum;
}
