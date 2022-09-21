package com.cmc.sparky.user.exception;

public class DuplicateNameException extends RuntimeException{
    public DuplicateNameException(String message){
        super(message);
    }
}
