package com.cmc.sparky.scrap.dto;

import lombok.Getter;

@Getter
public class TagResponse {
    private Long tagId;
    private String name;
    private String color;
    public TagResponse(Long tagId, String name, String color){
        this.tagId=tagId;
        this.name=name;
        this.color=color;
    }
}
