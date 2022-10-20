package com.cmc.sparky.scrap.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class HomeResponse {
    private List<ScrapResponse> myScraps;
    private List<ScrapResponse> recScraps;
}
