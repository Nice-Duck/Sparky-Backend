package com.cmc.sparky.user.service;

import com.cmc.sparky.common.dto.SuccessResponse;
import com.cmc.sparky.user.domain.User;
import com.cmc.sparky.user.exception.DuplicateNameException;
import com.cmc.sparky.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public SuccessResponse dupName(String name){
        SuccessResponse successResponse=new SuccessResponse();
        if(userRepository.existsByNickname(name)){
            successResponse.setCode("0002");
            successResponse.setMessage("닉네임이 중복되었습니다.");
            successResponse.setResult(null);
        }
        else{
            successResponse.setCode("0000");
            successResponse.setMessage("사용 가능한 닉네임입니다.");
            successResponse.setResult(null);
        }
        return successResponse;
    }
    public User findUser(Long id){
        return userRepository.findById(id).orElse(null);
    }
}
