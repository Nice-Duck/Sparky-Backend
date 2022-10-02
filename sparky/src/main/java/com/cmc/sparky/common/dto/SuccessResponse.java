package com.cmc.sparky.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse {
    private String code;
    private String message;
    private Object result;
}
