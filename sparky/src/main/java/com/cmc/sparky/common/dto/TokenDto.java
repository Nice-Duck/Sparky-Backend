package com.cmc.sparky.common.dto;

import lombok.Getter;

@Getter
public class TokenDto {
    private String refreshToken;
    private String accessToken;
    public TokenDto(String refreshToken, String accessToken){
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
    }
}
