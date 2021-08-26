package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetOrderCheckRes {
    private int deliveryTime;
    private String arrivalTime;
    private String orderNumber;
    private String address;
    private String requestToStore;
    private String requestToRider;
    private String storeName;
    private int foodNum;
    private String foodTxt;
    private int totalPrice;
}
