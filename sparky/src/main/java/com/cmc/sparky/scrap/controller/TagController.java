package com.cmc.sparky.scrap.controller;

import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.service.JwtService;
import com.cmc.sparky.scrap.dto.TagRequest;
import com.cmc.sparky.scrap.dto.TagResponse;
import com.cmc.sparky.scrap.dto.TagUpdateRequest;
import com.cmc.sparky.scrap.dto.TagsResponse;
import com.cmc.sparky.scrap.service.ScrapService;
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
    @ApiOperation(value="태그 수정하기",notes = "<strong>태그를 수정함</strong>")
    @PatchMapping("")
    public ResponseEntity<ServerResponse<TagResponse>> tagUpdate(@RequestHeader("Authorization") String token,
        @RequestBody TagUpdateRequest tagUpdateRequest){
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.updateTag(uid, tagUpdateRequest));
    }
    @ApiOperation(value="태그 삭제하기",notes = "<strong>태그를 삭제함</strong>")
    @DeleteMapping("/{id}")
    public ResponseEntity<ServerResponse<Void>> tagDelete(@RequestHeader("Authorization") String token,
        @PathVariable("id") Long tid){
        jwtService.validateToken(token);
        return ResponseEntity.ok().body(scrapService.deleteTag(tid));
    }
}
