package com.cmc.sparky.scrap.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UpdateRequest {
    private String title;
    private String subTitle;
    private String memo;
    private MultipartFile image;
    private String scpUrl;
    private List<Long> tags;


}
