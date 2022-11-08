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
@RequestMapping("/api/v1/scraps")
public class ScrapController {
    private final ScrapService scrapService;
    private final JwtService jwtService;
    private final UserService userService;
    @ApiOperation(value="스크랩 저장",notes = "<strong>스크랩을 저장한다.</strong>")
    @PostMapping("")
    public ResponseEntity<ServerResponse<SaveResponse>> scrapSave(@RequestHeader("Authorization") String token,
                                                                  @RequestBody ScrapRequest scrapRequest){
        jwtService.validateToken(token);
        Long userId=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.saveScrap(userService.findUser(userId),scrapRequest));
    }
    @ApiOperation(value="스크랩 수정",notes = "<strong>스크랩을 수정한다.</strong>")
    @PatchMapping("")
    public ResponseEntity<ServerResponse<Void>> scrapUpdate(@RequestHeader("Authorization") String token,
                                                                    @RequestParam("scrapId") Long scrapId
                                                                    ,@ModelAttribute UpdateRequest updateRequest) throws Exception {
        jwtService.validateToken(token);
        return ResponseEntity.ok().body(scrapService.updateScrap(scrapId, updateRequest));
    }

    @ApiOperation(value="스크랩 불러오기",notes = "<strong>스크랩을 불러온다.</strong>")
    @GetMapping("")
    public ResponseEntity<ServerResponse<HomeResponse>> scrapLoad(@RequestHeader("Authorization") String token,
                                                                  @RequestParam(value = "type",defaultValue = "0") Integer type){
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.loadScraps(uid,type));
    }

    @ApiOperation(value="스크랩 삭제하기",notes = "<strong>스크랩을 삭제한다.</strong>")
    @DeleteMapping("")
    public ResponseEntity<ServerResponse<Void>> scrapLoad(@RequestHeader("Authorization") String token,
                                                    @RequestParam("scrapId") Long scrapId) {
        jwtService.validateToken(token);
        return ResponseEntity.ok().body(scrapService.deleteScrap(scrapId));
    }


    @ApiOperation(value="스크랩 검색",notes = "<strong>스크랩 검색</strong>")
    @PostMapping("/search")
    public ResponseEntity<ServerResponse<List<ScrapResponse>>> scrapsSearch(@RequestHeader("Authorization") String token,
                                                                            @RequestBody SearchRequest searchRequest) {
        jwtService.validateToken(token);
        Long uid=jwtService.getUserId(token);
        return ResponseEntity.ok().body(scrapService.searchScraps(uid,searchRequest));
    }
    @ApiOperation(value="스크랩 url 유효성",notes = "<strong>스크랩 유효한지</strong>")
    @GetMapping("/validation")
    public ResponseEntity<ServerResponse<Void>> scrapsValidate(@RequestHeader("Authorization") String token,
                                                        @RequestParam("url")String url) {
        jwtService.validateToken(token);
        return ResponseEntity.ok().body(scrapService.urlValidator(url));
    }
    @ApiOperation(value="스크랩 신고",notes = "<strong>부적절한 스크랩 신고</strong>")
    @GetMapping("/declaration")
    public ResponseEntity<ServerResponse<Void>> scrapsDeclaration(@RequestHeader("Authorization") String token,
                                                                  @RequestParam("scrapId") Long scrapId) {
        jwtService.validateToken(token);
        return ResponseEntity.ok().body(scrapService.declareScraps(scrapId));
    }
}
