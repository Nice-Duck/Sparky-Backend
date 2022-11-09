package com.cmc.sparky.account.controller;

import com.cmc.sparky.account.dto.AuthRequest;
import com.cmc.sparky.account.dto.LoginRequest;
import com.cmc.sparky.account.dto.MailCheckRequest;
import com.cmc.sparky.account.dto.MailSendRequest;
import com.cmc.sparky.account.service.AccountService;
import com.cmc.sparky.account.service.MailService;
import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.dto.TokenDto;
import com.cmc.sparky.common.service.JwtService;
import com.cmc.sparky.scrap.dto.TagRequest;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;
    private final MailService mailService;
    private final JwtService jwtService;
    @ApiOperation(value="중복확인",notes = "<strong>이메일 중복을 확인한다.</strong>")
    @GetMapping("/register")
    public ResponseEntity<ServerResponse<Void>> duplicationUser(@RequestParam("email") String email){
        return ResponseEntity.ok().body(accountService.dupUser(email));
    }
    @ApiOperation(value="인증 메일 전송",notes = "<strong>인증메일전송</strong>")
    @PostMapping("/mails/send")
    public ResponseEntity<ServerResponse<Void>> checkUser(@RequestBody MailSendRequest mailSendRequest) {
        return ResponseEntity.ok().body(mailService.sendMail(mailSendRequest));
    }
    @ApiOperation(value="인증 메일 확인",notes = "<strong>인증메일확인</strong>")
    @PostMapping("/mails/confirm")
    public ResponseEntity<ServerResponse<Void>> confirmUser(@RequestBody MailCheckRequest mailCheckRequest){
        return ResponseEntity.ok().body(mailService.checkMail(mailCheckRequest));
    }
    @ApiOperation(value="회원가입",notes = "<strong>이메일과 패스워드를 입력받아 회원 가입을 진행한다.</strong>")
    @PostMapping("/register")
    public ResponseEntity<ServerResponse<TokenDto>> joinUser(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok().body(accountService.joinUser(authRequest));
    }
    @ApiOperation(value="로그인",notes = "<strong>이메일과 패스워드를 입력받아 성공 여부를 알린다.</strong>")
    @PostMapping("")
    public ResponseEntity<ServerResponse<TokenDto>> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok().body(accountService.checkUser(loginRequest));
    }

    @ApiOperation(value="회원탈퇴",notes = "<strong>회원 탈퇴</strong>")
    @DeleteMapping("")
    public ResponseEntity<ServerResponse<Void>> deleteUser(@RequestHeader("Authorization") String token){
        jwtService.validateToken(token);
        String email=jwtService.getUser(token);
        return ResponseEntity.ok().body(accountService.outUser(email));
    }
    /*
    @ApiOperation(value="비밀번호수정",notes = "<strong>회원수정</strong>")
    @RequestMapping(value="/api/v1/accounts", method = RequestMethod.PATCH)
    public ResponseEntity updateUser(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok().build();
    }*/
}
