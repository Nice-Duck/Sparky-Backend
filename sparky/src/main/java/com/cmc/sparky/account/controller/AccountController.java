package com.cmc.sparky.account.controller;

import com.cmc.sparky.account.dto.AuthRequest;
import com.cmc.sparky.account.service.AccountService;
import com.cmc.sparky.account.service.MailService;
import com.cmc.sparky.common.dto.TokenResponse;
import com.cmc.sparky.common.service.JwtService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class AccountController {
    private final AccountService accountService;
    private final MailService mailService;
    @ApiOperation(value="중복확인",notes = "<strong>이메일 중복을 확인한다.</strong>")
    @RequestMapping(value="/api/v1/accounts/register", method = RequestMethod.GET)
    public ResponseEntity duplicationUser(@RequestParam("email") String email){
        accountService.dupUser(email);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value="인증 메일 전송",notes = "<strong>인증메일전송</strong>")
    @RequestMapping(value="/api/v1/accounts/confirm", method = RequestMethod.GET)
    public ResponseEntity checkUser(@RequestParam("email") String email){
        mailService.sendMail(email);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value="회원가입",notes = "<strong>이메일과 패스워드를 입력받아 회원 가입을 진행한다.</strong>")
    @RequestMapping(value="/api/v1/accounts/register", method = RequestMethod.POST)
    public ResponseEntity<TokenResponse> joinUser(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok().body(accountService.joinUser(authRequest));
    }

    @ApiOperation(value="로그인",notes = "<strong>이메일과 패스워드를 입력받아 성공 여부를 알린다.</strong>")
    @RequestMapping(value="/api/v1/accounts", method = RequestMethod.POST)
    public ResponseEntity<TokenResponse> loginUser(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok().body(accountService.checkUser(authRequest));
    }

    @ApiOperation(value="회원탈퇴",notes = "<strong>회원 탈퇴</strong>")
    @RequestMapping(value="/api/v1/accounts", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@RequestBody AuthRequest authRequest){
        accountService.outUser(authRequest);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value="비밀번호수정",notes = "<strong>회원수정</strong>")
    @RequestMapping(value="/api/v1/accounts", method = RequestMethod.PATCH)
    public ResponseEntity updateUser(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok().build();
    }
}
