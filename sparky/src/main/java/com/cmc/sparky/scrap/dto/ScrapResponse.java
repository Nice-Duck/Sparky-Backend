package com.cmc.sparky.scrap.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class ScrapResponse {
    private String title;
    private String memo;
    private String imgUrl;
    private String scpUrl;
    private List<TagResponse> tagsResponse;
    public ScrapResponse(String title, String memo, String imgUrl, String scpUrl,
                         List<TagResponse> tagsResponse){
        this.title=title;
        this.memo=memo;
        this.imgUrl=imgUrl;
        this.scpUrl=scpUrl;
        this.tagsResponse=tagsResponse;
    }
}
