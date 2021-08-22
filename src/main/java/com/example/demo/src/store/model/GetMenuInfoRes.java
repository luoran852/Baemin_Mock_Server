package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetMenuInfoRes {
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
