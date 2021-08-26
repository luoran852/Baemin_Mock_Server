package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetKeepListRes {
    private int storeIdx;
    private String storePosterUrl;
    private String storeName;
    private int isPacking;
    private int isNew;
    private int isCoupon;
    private float rating;
    private int reviewNum;
    private List<String> mainMenu;
    private int deliMinOrderPrice;
    private int deliveryTip;
}
