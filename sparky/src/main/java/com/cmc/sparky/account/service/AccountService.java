package com.cmc.sparky.account.service;

import com.cmc.sparky.account.domain.Account;
import com.cmc.sparky.account.dto.AuthRequest;
import com.cmc.sparky.account.dto.LoginRequest;
import com.cmc.sparky.account.exception.DuplicateEmailException;
import com.cmc.sparky.account.exception.EmailException;
import com.cmc.sparky.account.exception.PasswordException;
import com.cmc.sparky.account.exception.WithdrawException;
import com.cmc.sparky.account.repository.AccountRepository;
import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.dto.TokenDto;
import com.cmc.sparky.common.service.JwtService;
import com.cmc.sparky.user.domain.User;
import com.cmc.sparky.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private ServerResponse serverResponse =new ServerResponse();
    public ServerResponse checkUser(LoginRequest loginRequest){
        Account account=accountRepository.findByEmail(loginRequest.getEmail());

        if(!accountRepository.existsByEmail(loginRequest.getEmail()) ){
            throw new EmailException("없는 이메일입니다.");
        }
        if(!passwordEncoder.matches(loginRequest.getPwd(),account.getPassword())){
            throw new PasswordException("비밀번호가 일치하지 않습니다.");
        }
        if(account.getUsed()==0){
            throw new WithdrawException("회원 탈퇴한 이메일입니다.");
        }
        TokenDto tokens = jwtService.createToken(account.getUser());
        serverResponse.setMessage("로그인에 성공했습니다.");
        serverResponse.setResult(tokens);

        return serverResponse;
    }

    public ServerResponse joinUser(AuthRequest authRequest){
        Account account =new Account();
        User user=new User();
        user.setNickname(authRequest.getNickname());
        userRepository.save(user);

        account.setUser(user);
        account.setEmail(authRequest.getEmail());
        account.setPassword(passwordEncoder.encode(authRequest.getPwd()));
        accountRepository.save(account);
        TokenDto tokens=jwtService.createToken(user);

        serverResponse.setMessage("회원가입에 성공했습니다.");
        serverResponse.setResult(tokens);
        return serverResponse;
    }
    public ServerResponse dupUser(String email){
        if(accountRepository.existsByEmail(email))
            throw new DuplicateEmailException("이메일이 중복되었습니다.");
        serverResponse.setMessage("가입 가능한 메일입니다.");
        return serverResponse;
    }
    public ServerResponse outUser(String email){
        Account user=accountRepository.findByEmail(email);
        user.setUsed(0);
        accountRepository.save(user);
        serverResponse.setMessage("회워탈퇴에 성공했습니다.");
        return serverResponse;
    }
}
