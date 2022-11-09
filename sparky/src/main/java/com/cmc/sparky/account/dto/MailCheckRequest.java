package com.cmc.sparky.account.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailCheckRequest {
    @ApiModelProperty(value = "이메일", example = "kmj123@naver.com", required = true)
    private String email;
    @ApiModelProperty(value = "인증번호", example = "123456", required = true)
    private String number;
}
