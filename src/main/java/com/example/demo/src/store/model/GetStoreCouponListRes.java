package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetStoreCouponListRes {
    private int couponIdx;
    private int couponPrice;
    private int isDelivery;
    private int isPacking;
    private int minOrder;
    private int validity;
    private int isDownloaded;
}