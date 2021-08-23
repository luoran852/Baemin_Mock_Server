package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetNewStoreListRes {
    private int storeIdx;
    private String storePosterUrl;
    private String storeName;
    private int isPacking;
    private int isNew;
    private int isCoupon;
    private float rating;
    private int deliveryTip;
    private int deliveryTime;
}
