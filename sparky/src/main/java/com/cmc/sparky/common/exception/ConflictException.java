package com.cmc.sparky.common.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException{
    private ErrorCode errorCode;
    public ConflictException(ErrorCode errorCode){
        this.errorCode=errorCode;
    }
}
