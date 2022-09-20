package com.cmc.sparky.account.service;

import com.cmc.sparky.account.domain.Account;
import com.cmc.sparky.account.dto.AuthRequest;
import com.cmc.sparky.account.exception.DuplicateEmailException;
import com.cmc.sparky.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    /*
    private final JwtService jwtService;
    public LoginResponse checkUser(AuthRequest authRequest){
        if(!loginRepository.existsByEmail(authRequest.getEmail())){
            throw new NoneEmailException("존재하지 않는 아이디입니다.");
        }
        Login user=loginRepository.findByEmail(authRequest.getEmail());
        if(!passwordEncoder.matches(authRequest.getPwd(),user.getPassword())){
            throw new NonePwdException("패스워드가 불일치합니다.");
        }
        if(user.getUsed()==0){
            throw new WithdrawException("회원탈퇴한 유저입니다.");
        }
        Member member=memberRepository.findByLogin(user);
        if(member==null){
            throw new NoneMemberException("맴버정보를 입력하지 않았습니다.");
        }
        TokenDto tokens=jwtService.createToken(authRequest.getEmail(), member);
        return new LoginResponse(tokens);
    }

     */
    public void joinUser(AuthRequest authRequest){
        if(accountRepository.existsByEmail(authRequest.getEmail())){
            throw new DuplicateEmailException("아이디가 중복되었습니다.");
        }
        Account user=new Account();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPwd()));
        accountRepository.save(user);
    }
}
