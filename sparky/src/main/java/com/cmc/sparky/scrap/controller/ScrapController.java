package com.cmc.sparky.scrap.controller;

import com.cmc.sparky.common.dto.SuccessResponse;
import com.cmc.sparky.common.service.JwtService;
import com.cmc.sparky.scrap.dto.ScrapRequest;
import com.cmc.sparky.scrap.dto.TagRequest;
import com.cmc.sparky.scrap.service.ScrapService;
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
    public ResponseEntity<SuccessResponse> scrapSave(@RequestHeader("Authorization") String token,
                                                     @RequestBody ScrapRequest scrapRequest){
        jwtService.validateToken(token);
        Long userId=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.saveScrap(userService.findUser(userId),scrapRequest));
    }

    @ApiOperation(value="스크랩 불러오기",notes = "<strong>자신의 스크랩을 불러온다.</strong>")
    @RequestMapping(value="/api/v1/scraps", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> scrapLoad(@RequestHeader("Authorization") String token){
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.loadScraps(uid));
    }


    @ApiOperation(value="모든 태그 조회",notes = "<strong>자신이 등록한 태그 전부 조회 (최신순)</strong>")
    @RequestMapping(value="/api/v1/tags", method = RequestMethod.GET)
    public ResponseEntity<SuccessResponse> curtagLoad(@RequestHeader("Authorization") String token){
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.loadLastTags(uid));
    }

    @ApiOperation(value="태그 저장하기",notes = "<strong>태그가 없는 경우 저장함</strong>")
    @RequestMapping(value="/api/v1/tags", method = RequestMethod.POST)
    public ResponseEntity<SuccessResponse> tagSave(@RequestHeader("Authorization") String token,
                                                 @RequestBody TagRequest tagRequest){
        jwtService.validateToken(token);
        return ResponseEntity.ok().body(scrapService.saveTag(tagRequest));
    }
}
