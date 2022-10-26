package com.cmc.sparky.user.service;

import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.user.domain.User;
import com.cmc.sparky.user.exception.DuplicateNameException;
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
            throw new DuplicateNameException("닉네임이 중복되었습니다.");
        }
        serverResponse.setMessage("사용 가능한 닉네임입니다.");
        return serverResponse;
    }
    public User findUser(Long id){
        return userRepository.findById(id).orElse(null);
    }
}
