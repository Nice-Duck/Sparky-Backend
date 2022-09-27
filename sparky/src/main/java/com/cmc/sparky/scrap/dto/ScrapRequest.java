package com.cmc.sparky.scrap.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScrapRequest {
    private String title;
    private String memo;
    private String imgUrl;
    private String scpUrl;
}
