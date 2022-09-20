package com.cmc.sparky.account.service;

import com.cmc.sparky.account.domain.Account;
import com.cmc.sparky.account.dto.AuthRequest;
import com.cmc.sparky.account.exception.DuplicateEmailException;
import com.cmc.sparky.account.exception.IncorrectException;
import com.cmc.sparky.account.exception.WithdrawException;
import com.cmc.sparky.account.repository.AccountRepository;
import com.cmc.sparky.common.dto.TokenDto;
import com.cmc.sparky.common.dto.TokenResponse;
import com.cmc.sparky.common.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenResponse checkUser(AuthRequest authRequest){
        Account user=accountRepository.findByEmail(authRequest.getEmail());
        if(!accountRepository.existsByEmail(authRequest.getEmail()) ||
                !passwordEncoder.matches(authRequest.getPwd(),user.getPassword())){
            throw new IncorrectException("아이디 또는 패스워드가 틀립니다.");
        }
        if(user.getUsed()==0){
            throw new WithdrawException("회원탈퇴한 유저입니다.");
        }
        TokenDto tokens=jwtService.createToken(user);
        return new TokenResponse(tokens);
    }

    public TokenResponse joinUser(AuthRequest authRequest){
        if(accountRepository.existsByEmail(authRequest.getEmail())){
            throw new DuplicateEmailException("아이디가 중복되었습니다.");
        }
        Account user=new Account();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPwd()));
        accountRepository.save(user);
        TokenDto tokens=jwtService.createToken(user);
        return new TokenResponse(tokens);
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
