package com.cmc.sparky.account.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @ApiModelProperty(value = "이메일", example = "kmj123@naver.com", required = true)
    private String email;
    @ApiModelProperty(value = "비밀번호", example = "kmj1234!", required = true)
    private String pwd;
}
