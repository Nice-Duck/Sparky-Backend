package com.cmc.sparky.scrap.exception;

import com.cmc.sparky.common.dto.ServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice("com.cmc.sparky.scrap")
public class ScrapExceptionHandler {
    private ServerResponse serverResponse=new ServerResponse();
    @ExceptionHandler(DupTagException.class)
    public ResponseEntity<ServerResponse> NotFoundTagException(Exception e){
        serverResponse.setCode("0008");
        serverResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(serverResponse);
    }
}
