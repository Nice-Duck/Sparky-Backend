package com.cmc.sparky.account.service;

import com.cmc.sparky.account.domain.Account;
import com.cmc.sparky.account.dto.AuthRequest;
import com.cmc.sparky.account.dto.LoginRequest;
import com.cmc.sparky.account.exception.IncorrectException;
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

    public ServerResponse checkUser(LoginRequest loginRequest){
        ServerResponse serverResponse =new ServerResponse();
        Account account=accountRepository.findByEmail(loginRequest.getEmail());

        if(!accountRepository.existsByEmail(loginRequest.getEmail()) ){
            serverResponse.setCode("0004");
            serverResponse.setMessage("없는 이메일입니다.");
            serverResponse.setResult(null);
        }
        else if(!passwordEncoder.matches(loginRequest.getPwd(),account.getPassword())){
            serverResponse.setCode("0005");
            serverResponse.setMessage("비밀번호가 일치하지 않습니다.");
            serverResponse.setResult(null);
        }
        else if(account.getUsed()==0){
            serverResponse.setCode("0006");
            serverResponse.setMessage("회원 탈퇴한 이메일입니다.");
            serverResponse.setResult(null);
        }
        else {
            TokenDto tokens = jwtService.createToken(account.getUser());
            serverResponse.setCode("0000");
            serverResponse.setMessage("로그인에 성공했습니다.");
            serverResponse.setResult(tokens);
        }
        return serverResponse;
    }

    public ServerResponse joinUser(AuthRequest authRequest){
        ServerResponse serverResponse =new ServerResponse();
        Account account =new Account();
        User user=new User();
        user.setNickname(authRequest.getNickname());
        userRepository.save(user);

        account.setUser(user);
        account.setEmail(authRequest.getEmail());
        account.setPassword(passwordEncoder.encode(authRequest.getPwd()));
        accountRepository.save(account);
        TokenDto tokens=jwtService.createToken(user);

        serverResponse.setCode("0000");
        serverResponse.setMessage("회원가입에 성공했습니다.");
        serverResponse.setResult(tokens);
        return serverResponse;
    }
    public ServerResponse dupUser(String email){
        ServerResponse serverResponse =new ServerResponse();
        if(accountRepository.existsByEmail(email)){
            serverResponse.setCode("0001");
            serverResponse.setMessage("이메일이 중복되었습니다.");
            serverResponse.setResult(null);
        }
        else{
            serverResponse.setCode("0000");
            serverResponse.setMessage("가입 가능한 메일입니다.");
            serverResponse.setResult(null);
        }
        return serverResponse;
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
