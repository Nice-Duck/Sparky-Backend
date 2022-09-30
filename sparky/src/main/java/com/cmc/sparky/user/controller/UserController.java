package com.cmc.sparky.user.controller;

import com.cmc.sparky.user.repository.UserRepository;
import com.cmc.sparky.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @ApiOperation(value="중복확인",notes = "<strong>닉네임 중복을 확인한다.</strong>")
    @RequestMapping(value="/api/v1/users", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 409, message = "동일 이름이 존재합니다.")
    })
    public ResponseEntity userRegister(@RequestParam("name") String name){
        userService.dupName(name);
        return ResponseEntity.ok().build();
    }

}
