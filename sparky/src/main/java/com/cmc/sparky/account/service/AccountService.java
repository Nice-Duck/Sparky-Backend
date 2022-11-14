package com.cmc.sparky.account.service;

import com.cmc.sparky.account.domain.Account;
import com.cmc.sparky.account.dto.AuthRequest;
import com.cmc.sparky.account.dto.LoginRequest;
import com.cmc.sparky.account.repository.AccountRepository;
import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.dto.TokenDto;
import com.cmc.sparky.common.exception.ConflictException;
import com.cmc.sparky.common.exception.ErrorCode;
import com.cmc.sparky.common.exception.NotFoundException;
import com.cmc.sparky.common.exception.UnauthorizedException;
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
            throw new ConflictException(ErrorCode.INVALID_EMAIL);
        }
        if(!passwordEncoder.matches(loginRequest.getPwd(),account.getPassword())){
            throw new ConflictException(ErrorCode.INVALID_PASSWORD);
        }
        if(account.getUsed()==0){
            throw new NotFoundException(ErrorCode.INVALID_USER);
        }
        TokenDto tokens = jwtService.createToken(account.getUser());
        return serverResponse.success("로그인에 성공했습니다.",tokens);
    }

    public ServerResponse joinUser(AuthRequest authRequest) {
        Account account =new Account();
        User user=new User();
        user.setNickname(authRequest.getNickname());
        userRepository.save(user);

        account.setUser(user);
        account.setEmail(authRequest.getEmail());
        account.setPassword(passwordEncoder.encode(authRequest.getPwd()));
        accountRepository.save(account);
        TokenDto tokens=jwtService.createToken(user);

        return serverResponse.success("회원가입에 성공했습니다.",tokens);
    }
    public ServerResponse dupUser(String email){
        Account account=accountRepository.findByEmail(email);
        if(account==null) return serverResponse.success("가입 가능한 메일입니다.");
        else{
            if(account.getUsed()==0) throw new NotFoundException(ErrorCode.INVALID_USER);
            else throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);
        }
    }
    public ServerResponse outUser(String email){
        Account user=accountRepository.findByEmail(email);
        user.setUsed(0);
        accountRepository.save(user);
        return serverResponse.success("회원탈퇴에 성공했습니다.");
    }
    public ServerResponse updatePwd(LoginRequest loginRequest){
        Account account=accountRepository.findByEmail(loginRequest.getEmail());
        account.setPassword(passwordEncoder.encode(loginRequest.getPwd()));
        accountRepository.save(account);
        return serverResponse.success("비밀번호를 수정했습니다. 다시 로그인을 진행해주세요.");
    }
}
