package com.cmc.sparky.scrap.dto;

import lombok.Getter;

@Getter
public class TagIdResponse {
    private Long tagId;
    public TagIdResponse(Long tagId){
        this.tagId=tagId;
    }
}
