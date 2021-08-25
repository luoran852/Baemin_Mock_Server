package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class PostOrderReq {
    private int totalPrice;
    private String requestToStore;
    private String requestToRider;
    private int needSpoon;
    private int payMethodIdx;
    private String payMethodTxt;
    private int usedPayMoney;
    private int usedPoint;
    private int usedCouponIdx;
    private int totalFoodNum;
    private List<OrderFoodListObject> foodList;
}