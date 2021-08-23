package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetVisitStoreListRes {
    private int storeIdx;
    private String storeName;
    private String foodPosterUrl;
    private int deliveryTime;
    private GetVisitCateRes menuInfo;
}
