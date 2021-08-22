package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreMenuRes {
    private Object menuInfo;
    private int foodIdx;
    private String foodTxt;
    private int foodTypeIdx;
    private String foodComment;
    private int foodPrice;
    private String foodImgUrl;
    private int isPopular;
    private int isSoldOut;
    private int isAlcohol;
}
