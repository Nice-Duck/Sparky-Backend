package com.cmc.sparky.common.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice("com.cmc.sparky")
public class JwtExceptionHandler {
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Map<String, String>> TokenExpried(Exception e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("message",e.getMessage()));
    }
}