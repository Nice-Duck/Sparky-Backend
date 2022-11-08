package com.cmc.sparky.common.exception;


import com.cmc.sparky.common.dto.ServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice("com.cmc.sparky")
public class FailExceptionHandler {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ServerResponse> conflictHandle(ConflictException e){
        ErrorCode errorCode=e.getErrorCode();
        ServerResponse serverResponse=ServerResponse.of(errorCode);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(serverResponse);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ServerResponse> notFoundHandle(NotFoundException e){
        ErrorCode errorCode=e.getErrorCode();
        ServerResponse serverResponse=ServerResponse.of(errorCode);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(serverResponse);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ServerResponse> unauthorizedHandle(UnauthorizedException e){
        ErrorCode errorCode=e.getErrorCode();
        ServerResponse serverResponse=ServerResponse.of(errorCode);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(serverResponse);
    }
    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ServerResponse> serverHandle(UnauthorizedException e){
        ErrorCode errorCode=e.getErrorCode();
        ServerResponse serverResponse=ServerResponse.of(errorCode);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(serverResponse);
    }
}
