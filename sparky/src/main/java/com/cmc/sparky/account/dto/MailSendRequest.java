package com.cmc.sparky.account.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailSendRequest {
    @ApiModelProperty(value = "이메일", example = "kmj123@naver.com", required = true)
    private String email;
    @ApiModelProperty(value = "type", example = "0", required = true)
    private Integer type=0;
}
