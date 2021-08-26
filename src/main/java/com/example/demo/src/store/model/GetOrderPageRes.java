package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class GetOrderPageRes {
    private String address;
    private String phoneNum;
    private int payMoney;
    private int pointSavePrice;
    private List<GetOrderingCouponListRes> couponList;
}
