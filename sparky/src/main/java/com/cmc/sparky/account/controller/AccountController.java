package com.cmc.sparky.account.controller;

import com.cmc.sparky.account.dto.AuthRequest;
import com.cmc.sparky.account.service.AccountService;
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
    @ApiOperation(value="회원가입",notes = "<strong>이메일과 패스워드를 입력받아 회원 가입을 진행한다.</strong>")
    @RequestMapping(value="/api/v1/accounts/register", method = RequestMethod.POST)
    public ResponseEntity<TokenResponse> userRegister(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok().body(accountService.joinUser(authRequest));
    }

    @ApiOperation(value="로그인",notes = "<strong>이메일과 패스워드를 입력받아 성공 여부를 알린다.</strong>")
    @RequestMapping(value="/api/v1/accounts", method = RequestMethod.POST)
    public ResponseEntity<TokenResponse> userLogin(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok().body(accountService.checkUser(authRequest));
    }

    @ApiOperation(value="회원탈퇴",notes = "<strong>회원 탈퇴</strong>")
    @RequestMapping(value="/api/v1/accounts", method = RequestMethod.DELETE)
    public ResponseEntity userDelete(@RequestBody AuthRequest authRequest){
        accountService.outUser(authRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="비밀번호수정",notes = "<strong>회원수정</strong>")
    @RequestMapping(value="/api/v1/accounts", method = RequestMethod.PATCH)
    public ResponseEntity userUpdate(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok().build();
    }
}
