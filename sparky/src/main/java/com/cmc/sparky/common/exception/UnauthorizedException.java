package com.cmc.sparky.common.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException{
    private ErrorCode errorCode;
    public UnauthorizedException(ErrorCode errorCode){
        this.errorCode=errorCode;
    }
}
