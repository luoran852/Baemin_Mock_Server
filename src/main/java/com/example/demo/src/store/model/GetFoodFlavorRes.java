package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetFoodFlavorRes {
    private int flavorIdx;
    private String flavorTxt;
    private int flavorPrice;
}
