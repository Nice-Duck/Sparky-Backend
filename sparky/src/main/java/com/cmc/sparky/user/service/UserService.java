package com.cmc.sparky.user.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.exception.ConflictException;
import com.cmc.sparky.common.exception.ErrorCode;
import com.cmc.sparky.user.domain.User;
import com.cmc.sparky.user.dto.UserRequest;
import com.cmc.sparky.user.dto.UserResponse;
import com.cmc.sparky.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UserService {
    private String S3Bucket = "sparkyapp"; // Bucket 이름


    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;
    private ServerResponse serverResponse=new ServerResponse();
    public ServerResponse dupName(String name){
        if(userRepository.existsByNickname(name)){
            throw new ConflictException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return serverResponse.success("사용 가능한 닉네임입니다.");
    }
    public ServerResponse loadUser(Long uid){
        User user=userRepository.findById(uid).orElse(null);
        UserResponse userResponse=new UserResponse(user.getNickname(),user.getIcon());
        return serverResponse.success("사용자 이름과 아이콘 주소를 불러왔습니다.",userResponse);
    }
    public ServerResponse updateUser(Long uid, UserRequest userRequest) throws Exception {
        MultipartFile multipartFile=userRequest.getIcon();
        String originalName = multipartFile.getOriginalFilename(); // 파일 이름
        long size = multipartFile.getSize(); // 파일 크기
        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(size);
        // S3에 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        String imagePath = amazonS3Client.getUrl(S3Bucket, originalName).toString(); // 접근가능한 URL 가져오기
        User user=userRepository.findById(uid).orElse(null);
        user.setIcon(imagePath);
        userRepository.save(user);
        UserResponse userResponse=new UserResponse(user.getNickname(),user.getIcon());
        return serverResponse.success("프로필을 수정했습니다.",userResponse);
    }
    public User findUser(Long id){
        return userRepository.findById(id).orElse(null);
    }
}
