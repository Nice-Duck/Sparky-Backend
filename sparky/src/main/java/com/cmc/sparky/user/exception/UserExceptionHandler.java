package com.cmc.sparky.user.exception;

import com.cmc.sparky.common.dto.ServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice("com.cmc.sparky.user")
public class UserExceptionHandler {
    private ServerResponse serverResponse=new ServerResponse();
    @ExceptionHandler(DuplicateNameException.class)
    public ResponseEntity<ServerResponse> nameAlreadyExistException(Exception e){
        serverResponse.setCode("0002");
        serverResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(serverResponse);
    }
}
