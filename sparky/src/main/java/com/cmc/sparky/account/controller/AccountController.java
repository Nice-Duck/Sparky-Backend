package com.cmc.sparky.account.controller;

import com.cmc.sparky.account.dto.AuthRequest;
import com.cmc.sparky.account.dto.LoginRequest;
import com.cmc.sparky.account.dto.MailCheckRequest;
import com.cmc.sparky.account.dto.MailSendRequest;
import com.cmc.sparky.account.service.AccountService;
import com.cmc.sparky.account.service.MailService;
import com.cmc.sparky.common.dto.TokenResponse;
import com.cmc.sparky.common.service.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final MailService mailService;
    @ApiOperation(value="중복확인",notes = "<strong>이메일 중복을 확인한다.</strong>")
    @RequestMapping(value="/api/v1/accounts/register", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 409, message = "아이디가 중복되었습니다.")
    })
    public ResponseEntity duplicationUser(@RequestParam("email") String email){
        accountService.dupUser(email);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value="인증 메일 전송",notes = "<strong>인증메일전송</strong>")
    @RequestMapping(value="/api/v1/accounts/mails/send", method = RequestMethod.POST)
    public ResponseEntity checkUser(@RequestBody MailSendRequest mailSendRequest) {
        mailService.sendMail(mailSendRequest);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value="인증 메일 확인",notes = "<strong>인증메일확인</strong>")
    @RequestMapping(value="/api/v1/accounts/mails/confirm", method = RequestMethod.POST)

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "인증 번호가 일치하지 않습니다.")
    })
    public ResponseEntity confirmUser(@RequestBody MailCheckRequest mailCheckRequest){
        mailService.checkMail(mailCheckRequest);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value="회원가입",notes = "<strong>이메일과 패스워드를 입력받아 회원 가입을 진행한다.</strong>")
    @RequestMapping(value="/api/v1/accounts/register", method = RequestMethod.POST)
    public ResponseEntity<TokenResponse> joinUser(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok().body(accountService.joinUser(authRequest));
    }

    @ApiOperation(value="로그인",notes = "<strong>이메일과 패스워드를 입력받아 성공 여부를 알린다.</strong>")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "탈퇴한 회원입니다."),
            @ApiResponse(code = 409, message = "아이디 또는 패스워드가 틀립니다.")
    })
    @RequestMapping(value="/api/v1/accounts", method = RequestMethod.POST)
    public ResponseEntity<TokenResponse> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok().body(accountService.checkUser(loginRequest));
    }

    @ApiOperation(value="회원탈퇴",notes = "<strong>회원 탈퇴</strong>")
    @RequestMapping(value="/api/v1/accounts", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@RequestBody AuthRequest authRequest){
        accountService.outUser(authRequest);
        return ResponseEntity.ok().build();
    }
    /*
    @ApiOperation(value="비밀번호수정",notes = "<strong>회원수정</strong>")
    @RequestMapping(value="/api/v1/accounts", method = RequestMethod.PATCH)
    public ResponseEntity updateUser(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok().build();
    }*/
}
