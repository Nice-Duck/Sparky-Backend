package com.cmc.sparky.user.service;

import com.cmc.sparky.user.exception.DuplicateNameException;
import com.cmc.sparky.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void dupName(String name){
        if(userRepository.existsByNickname(name)){
            throw new DuplicateNameException("동일 이름이 존재합니다.");
        }
    }
}
