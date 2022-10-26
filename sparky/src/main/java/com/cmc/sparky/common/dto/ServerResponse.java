package com.cmc.sparky.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ServerResponse {
    private String code="0000";
    private String message;
    private Object result=null;
}
