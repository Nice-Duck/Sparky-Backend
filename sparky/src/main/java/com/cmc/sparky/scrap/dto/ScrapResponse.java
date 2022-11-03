package com.cmc.sparky.scrap.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class ScrapResponse {
    private Integer type;
    private Long scrapId;
    private String title;
    private String subTitle;
    private String memo;

    private String imgUrl;
    private String scpUrl;
    private List<TagResponse> tagsResponse;
    public ScrapResponse(Integer type, Long scrapId, String title, String subTitle, String memo, String imgUrl, String scpUrl,
                         List<TagResponse> tagsResponse){
        this.type=type;
        this.scrapId=scrapId;
        this.subTitle=subTitle;
        this.title=title;
        this.memo=memo;
        this.imgUrl=imgUrl;
        this.scpUrl=scpUrl;
        this.tagsResponse=tagsResponse;
    }
}
