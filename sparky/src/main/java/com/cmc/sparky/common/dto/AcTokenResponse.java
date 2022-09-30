package com.cmc.sparky.common.dto;

import lombok.Getter;

@Getter
public class AcTokenResponse {
    private String accessToken;
    public AcTokenResponse(String accessToken){
        this.accessToken=accessToken;
    }
}
