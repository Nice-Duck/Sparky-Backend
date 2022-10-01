package com.cmc.sparky.scrap.dto;

import lombok.Getter;

@Getter
public class TagResponse {
    private String name;
    private String color;
    public TagResponse(String name, String color){
        this.name=name;
        this.color=color;
    }
}
