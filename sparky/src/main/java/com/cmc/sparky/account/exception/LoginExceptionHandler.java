package com.cmc.sparky.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice("com.cmc.sparky.account.controller")
public class LoginExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, String>> emailAlreadyExistException(Exception e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message",e.getMessage()));
    }
    @ExceptionHandler(IncorrectException.class)
    public ResponseEntity<Map<String, String>> NotExistEmailException(Exception e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message",e.getMessage()));
    }
    @ExceptionHandler(WithdrawException.class)
    public ResponseEntity<Map<String, String>> OutMemberException(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message",e.getMessage()));
    }
}
