package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class GetFoodInfoRes {
    private String foodImgUrl;
    private String foodTxt;
    private String foodComponents;
    private int deliMinOrderPrice;
    private int foodPrice;
    private List flavor;
}