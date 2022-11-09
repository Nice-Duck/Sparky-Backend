package com.cmc.sparky.common.exception;

public class ServerException extends RuntimeException{
    private ErrorCode errorCode;
    public ServerException(){
        this.errorCode=ErrorCode.INTERNAL_ERROR;
    }
}
