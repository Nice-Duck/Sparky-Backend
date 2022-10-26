package com.cmc.sparky.account.exception;

import com.cmc.sparky.common.dto.ServerResponse;
import org.apache.catalina.Server;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice("com.cmc.sparky.account")
public class LoginExceptionHandler {
    private ServerResponse serverResponse=new ServerResponse();
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ServerResponse> emailAlreadyExistException(Exception e){
        serverResponse.setCode("0001");
        serverResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(serverResponse);
    }
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ServerResponse> NotExistEmailException(Exception e){
        serverResponse.setCode("0005");
        serverResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(serverResponse);
    }
    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ServerResponse> NotPwdException(Exception e){
        serverResponse.setCode("0005");
        serverResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(serverResponse);
    }
    @ExceptionHandler(WithdrawException.class)
    public ResponseEntity<ServerResponse> OutMemberException(Exception e){
        serverResponse.setCode("1000");
        serverResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(serverResponse);
    }
}
