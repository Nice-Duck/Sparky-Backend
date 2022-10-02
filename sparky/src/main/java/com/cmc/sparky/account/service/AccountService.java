package com.cmc.sparky.account.service;

import com.cmc.sparky.account.domain.Account;
import com.cmc.sparky.account.dto.AuthRequest;
import com.cmc.sparky.account.dto.LoginRequest;
import com.cmc.sparky.account.exception.DuplicateEmailException;
import com.cmc.sparky.account.exception.IncorrectException;
import com.cmc.sparky.account.exception.WithdrawException;
import com.cmc.sparky.account.repository.AccountRepository;
import com.cmc.sparky.common.dto.SuccessResponse;
import com.cmc.sparky.common.dto.TokenDto;
import com.cmc.sparky.common.dto.TokenResponse;
import com.cmc.sparky.common.service.JwtService;
import com.cmc.sparky.user.domain.User;
import com.cmc.sparky.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public SuccessResponse checkUser(LoginRequest loginRequest){
        SuccessResponse successResponse=new SuccessResponse();
        Account account=accountRepository.findByEmail(loginRequest.getEmail());

        if(!accountRepository.existsByEmail(loginRequest.getEmail()) ){
            successResponse.setCode("0004");
            successResponse.setMessage("없는 이메일입니다.");
            successResponse.setResult(null);
        }
        else if(!passwordEncoder.matches(loginRequest.getPwd(),account.getPassword())){
            successResponse.setCode("0005");
            successResponse.setMessage("비밀번호가 일치하지 않습니다.");
            successResponse.setResult(null);
        }
        else if(account.getUsed()==0){
            successResponse.setCode("0006");
            successResponse.setMessage("회원 탈퇴한 이메일입니다.");
            successResponse.setResult(null);
        }
        else {
            TokenDto tokens = jwtService.createToken(account.getUser());
            successResponse.setCode("0000");
            successResponse.setMessage("로그인에 성공했습니다.");
            successResponse.setResult(tokens);
        }
        return successResponse;
    }

    public SuccessResponse joinUser(AuthRequest authRequest){
        SuccessResponse successResponse=new SuccessResponse();
        Account account =new Account();
        User user=new User();
        user.setNickname(authRequest.getNickname());
        userRepository.save(user);

        account.setUser(user);
        account.setEmail(authRequest.getEmail());
        account.setPassword(passwordEncoder.encode(authRequest.getPwd()));
        accountRepository.save(account);
        TokenDto tokens=jwtService.createToken(user);

        successResponse.setCode("0000");
        successResponse.setMessage("회원가입에 성공했습니다.");
        successResponse.setResult(tokens);
        return successResponse;
    }
    public SuccessResponse dupUser(String email){
        SuccessResponse successResponse=new SuccessResponse();
        if(accountRepository.existsByEmail(email)){
            successResponse.setCode("0001");
            successResponse.setMessage("이메일이 중복되었습니다.");
            successResponse.setResult(null);
        }
        else{
            successResponse.setCode("0000");
            successResponse.setMessage("가입 가능한 메일입니다.");
            successResponse.setResult(null);
        }
        return successResponse;
    }
    public void outUser(AuthRequest authRequest){
        Account user=accountRepository.findByEmail(authRequest.getEmail());
        if(!passwordEncoder.matches(authRequest.getPwd(),user.getPassword())){
            throw new IncorrectException("패스워드가 불일치합니다.");
        }
        user.setUsed(0);
        accountRepository.save(user);
    }
}
