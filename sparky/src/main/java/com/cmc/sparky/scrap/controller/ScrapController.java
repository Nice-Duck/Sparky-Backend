package com.cmc.sparky.scrap.controller;

import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.service.JwtService;
import com.cmc.sparky.scrap.dto.*;
import com.cmc.sparky.scrap.service.ScrapService;
import com.cmc.sparky.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;
    private final JwtService jwtService;
    private final UserService userService;
    @ApiOperation(value="스크랩 저장",notes = "<strong>스크랩을 저장한다.</strong>")
    @RequestMapping(value="/api/v1/scraps", method = RequestMethod.POST)
    public ResponseEntity<ServerResponse<SaveResponse>> scrapSave(@RequestHeader("Authorization") String token,
                                                                  @RequestBody ScrapRequest scrapRequest){
        jwtService.validateToken(token);
        Long userId=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.saveScrap(userService.findUser(userId),scrapRequest));
    }
    @ApiOperation(value="스크랩 수정",notes = "<strong>스크랩을 수정한다.</strong>")
    @RequestMapping(value="/api/v1/scraps", method = RequestMethod.PATCH)
    public ResponseEntity<ServerResponse<SaveResponse>> scrapUpdate(@RequestHeader("Authorization") String token,
                                                                    @RequestParam("scrapId") Long scrapId
                                                                    ,@RequestBody ScrapRequest scrapRequest){
        jwtService.validateToken(token);
        return ResponseEntity.ok().body(scrapService.updateScrap(scrapId, scrapRequest));
    }

    @ApiOperation(value="스크랩 불러오기",notes = "<strong>스크랩을 불러온다.</strong>")
    @RequestMapping(value="/api/v1/scraps", method = RequestMethod.GET)
    public ResponseEntity<ServerResponse<HomeResponse>> scrapLoad(@RequestHeader("Authorization") String token,
                                                                  @RequestParam(value = "type",defaultValue = "0") Integer type){
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.loadScraps(uid,type));
    }

    @ApiOperation(value="스크랩 삭제하기",notes = "<strong>스크랩을 삭제한다.</strong>")
    @RequestMapping(value="/api/v1/scraps", method = RequestMethod.DELETE)
    public ResponseEntity<ServerResponse<Void>> scrapLoad(@RequestHeader("Authorization") String token,
                                                    @RequestParam("scrapId") Long scrapId) {
        jwtService.validateToken(token);
        return ResponseEntity.ok().body(scrapService.deleteScrap(scrapId));
    }

    @ApiOperation(value="모든 태그 조회",notes = "<strong>자신이 등록한 태그 전부 조회 (최신순)</strong>")
    @RequestMapping(value="/api/v1/tags", method = RequestMethod.GET)
    public ResponseEntity<ServerResponse<TagsResponse>> tagSearch(@RequestHeader("Authorization") String token){
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.loadTags(uid));
    }

    @ApiOperation(value="태그 저장하기",notes = "<strong>태그가 없는 경우 저장함</strong>")
    @RequestMapping(value="/api/v1/tags", method = RequestMethod.POST)
    public ResponseEntity<ServerResponse<TagResponse>> tagSave(@RequestHeader("Authorization") String token,
                                                  @RequestBody TagRequest tagRequest){
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.saveTag(uid,tagRequest));
    }

    @ApiOperation(value="스크랩 검색",notes = "<strong>스크랩 검색</strong>")
    @RequestMapping(value="/api/v1/scraps/search", method = RequestMethod.POST)
    public ResponseEntity<ServerResponse<List<ScrapResponse>>> scrapsSearch(@RequestHeader("Authorization") String token,
                                                                            @RequestBody SearchRequest searchRequest) {
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.searchScraps(uid,searchRequest));
    }

}
