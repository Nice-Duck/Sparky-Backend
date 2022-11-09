package com.cmc.sparky.common.dto;

import lombok.Getter;

@Getter
public class TokenResponse {
    private TokenDto tokens;
    public TokenResponse(TokenDto tokens){
        this.tokens=tokens;
    }
}
