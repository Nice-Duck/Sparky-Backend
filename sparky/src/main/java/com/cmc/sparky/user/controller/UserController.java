package com.cmc.sparky.user.controller;

import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.service.JwtService;
import com.cmc.sparky.user.dto.InquiryRequest;
import com.cmc.sparky.user.dto.UserRequest;
import com.cmc.sparky.user.dto.UserResponse;
import com.cmc.sparky.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    @ApiOperation(value="중복확인",notes = "<strong>닉네임 중복을 확인한다.</strong>")
    @GetMapping("/duplication")
    public ResponseEntity<ServerResponse<Void>> userRegister(@RequestParam("name") String name){
        return ResponseEntity.ok().body(userService.dupName(name));
    }
    @ApiOperation(value="마이페이지 로드",notes = "<strong>닉네임, 아이콘 로드</strong>")
    @GetMapping("")
    public ResponseEntity<ServerResponse<UserResponse>> userLoad(@RequestHeader("Authorization") String token){
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(userService.loadUser(uid));
    }
    @ApiOperation(value="마이페이지 수정",notes = "<strong>수정 완료되었습니다.</strong>")
    @PatchMapping("")
    public ResponseEntity<ServerResponse<UserResponse>> userUpdate(@RequestHeader("Authorization") String token,
        @ModelAttribute UserRequest userRequest) throws Exception{
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(userService.updateUser(uid,userRequest));
    }
    @ApiOperation(value="문의사항",notes = "<strong>문의사항 입니다.</strong>")
    @PostMapping("/inquiry")
    public ResponseEntity<ServerResponse<Void>> userInquiry(@RequestHeader("Authorization") String token,
        @RequestBody InquiryRequest inquiryRequest){
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(userService.inquiryUser(uid,inquiryRequest));
    }

}
