package com.cmc.sparky.common.dto;

import com.cmc.sparky.common.exception.ErrorCode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class ServerResponse {
    private String code;
    private String message;
    private Object result;
    public ServerResponse() {
        this.code = "0000";
    }
    public ServerResponse(ErrorCode code, Object result) {
        this.message = code.getMessage();
        this.code = code.getCode();
        this.result = result;
    }
    public ServerResponse success(String message, Object result){
        this.message=message;
        this.result=result;
        return this;
    }
    public ServerResponse success(String message){
        this.message=message;
        this.result=null;
        return this;
    }
    public static ServerResponse of(ErrorCode code) {
        return new ServerResponse(code, null);
    }
}
