package com.cmc.sparky.scrap.controller;

import com.cmc.sparky.common.service.JwtService;
import com.cmc.sparky.scrap.dto.ScrapRequest;
import com.cmc.sparky.scrap.service.ScrapService;
import com.cmc.sparky.user.domain.User;
import com.cmc.sparky.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;
    private final JwtService jwtService;
    private final UserService userService;
    @ApiOperation(value="스크랩 저장",notes = "<strong>스크랩을 저장한다.</strong>")
    @RequestMapping(value="/api/v1/scraps", method = RequestMethod.POST)
    public ResponseEntity scrapSave(@RequestHeader("Authorization") String token,
                                    @RequestBody ScrapRequest scrapRequest){
        jwtService.validateToken(token);
        Long userId=jwtService.getUserId(token);
        scrapService.saveScrap(userService.findUser(userId),scrapRequest);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value="스크랩 불러오기",notes = "<strong>자신의 스크랩을 불러온다.</strong>")
    @RequestMapping(value="/api/v1/scraps", method = RequestMethod.GET)
    public ResponseEntity scrapLoad(@RequestParam("name") String name){

        return ResponseEntity.ok().build();
    }
}
