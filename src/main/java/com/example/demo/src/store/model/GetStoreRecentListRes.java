package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreRecentListRes {
    private int storeIdx;
    private String foodPosterUrl;
    private String storeName;
    private int isNew;
    private int isCoupon;
    private float rating;
    private int deliveryTip;
    private int deliveryTime;
}
