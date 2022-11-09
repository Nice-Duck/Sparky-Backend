package com.cmc.sparky.scrap.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TagsResponse {
    List<TagResponse> tagResponses;
    public TagsResponse(List<TagResponse> tagResponses){
        this.tagResponses=tagResponses;
    }
}
