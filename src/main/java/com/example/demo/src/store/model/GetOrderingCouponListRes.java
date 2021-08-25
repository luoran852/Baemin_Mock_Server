package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetOrderingCouponListRes {
    private int couponIdx;
    private int couponPrice;
    private String storeTxt;
    private int leftDate;
    private String expireDate;
}