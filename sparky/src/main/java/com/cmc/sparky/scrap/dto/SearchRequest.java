package com.cmc.sparky.scrap.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchRequest {
    private Integer type;
    private String title;
    private List<Long> tags=new ArrayList<>();
}
