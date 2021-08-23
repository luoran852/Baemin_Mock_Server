package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetBaemin1StoreListRes {
    private List<String> baeminOneImages;
    private int storeIdx;
    private String storeName;
    private float rating;
    private int deliveryTime;
    private int deliMinOrderPrice;
    private float storeDistance;
    private int deliveryTip;
    private int isNew;
    private int isCoupon;

}
