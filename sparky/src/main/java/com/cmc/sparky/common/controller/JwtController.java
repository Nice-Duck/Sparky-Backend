package com.cmc.sparky.common.controller;

import com.cmc.sparky.common.dto.AcTokenResponse;
import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.service.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class JwtController {
    private final JwtService jwtService;
    @ApiOperation(value="Access Token 새로 발급",notes = "<strong>refresh 토큰이 유효하다면 access 새로 발급</strong>")
    @RequestMapping(value="/api/v1/accounts/token", method = RequestMethod.POST)
    public ResponseEntity<ServerResponse<AcTokenResponse>> createAccessToken(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(jwtService.reissueAcToken(token));
    }
}
