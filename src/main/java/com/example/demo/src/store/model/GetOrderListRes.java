package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetOrderListRes {
    private int orderIdx;
    private int storeIdx;
    private String storePosterUrl;
    private String storeName;
    private String mainFoodTxt;
    private int orderedFoodNum;
    private int totalPrice;
    private String orderedDate;
}