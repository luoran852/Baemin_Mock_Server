package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class GetStoreMenuRes {
    private String menuInfo;
    private int foodTypeNum;
    private String foodOrigin;
    private List foodList;
}

