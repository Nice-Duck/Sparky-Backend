package com.cmc.sparky.user.service;

import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.exception.ConflictException;
import com.cmc.sparky.common.exception.ErrorCode;
import com.cmc.sparky.user.domain.User;
import com.cmc.sparky.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public ServerResponse dupName(String name){
        ServerResponse serverResponse =new ServerResponse();
        if(userRepository.existsByNickname(name)){
            throw new ConflictException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return serverResponse.success("사용 가능한 닉네임입니다.");
    }
    public User findUser(Long id){
        return userRepository.findById(id).orElse(null);
    }
}
