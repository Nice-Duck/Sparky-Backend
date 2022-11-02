package com.cmc.sparky.scrap.controller;

import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.service.JwtService;
import com.cmc.sparky.scrap.dto.TagRequest;
import com.cmc.sparky.scrap.dto.TagResponse;
import com.cmc.sparky.scrap.dto.TagsResponse;
import com.cmc.sparky.scrap.service.ScrapService;
import com.cmc.sparky.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController {
    private final ScrapService scrapService;
    private final JwtService jwtService;
    @ApiOperation(value="모든 태그 조회",notes = "<strong>자신이 등록한 태그 전부 조회 (최신순)</strong>")
    @GetMapping("")
    public ResponseEntity<ServerResponse<TagsResponse>> tagSearch(@RequestHeader("Authorization") String token){
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.loadTags(uid));
    }
    @ApiOperation(value="태그 저장하기",notes = "<strong>태그가 없는 경우 저장함</strong>")
    @PostMapping("")
    public ResponseEntity<ServerResponse<TagResponse>> tagSave(@RequestHeader("Authorization") String token,
                                                               @RequestBody TagRequest tagRequest){
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.saveTag(uid,tagRequest));
    }
}
