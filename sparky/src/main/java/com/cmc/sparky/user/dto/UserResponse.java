package com.cmc.sparky.user.dto;

import lombok.Getter;

@Getter
public class UserResponse {
    private String nickName;
    private String icon;
    public UserResponse(String nickName, String icon){
        this.nickName=nickName;
        this.icon=icon;
    }
}
