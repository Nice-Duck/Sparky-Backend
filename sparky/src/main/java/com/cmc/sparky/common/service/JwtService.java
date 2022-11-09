package com.cmc.sparky.common.service;

import com.cmc.sparky.account.domain.Account;
import com.cmc.sparky.account.repository.AccountRepository;
import com.cmc.sparky.common.domain.Token;
import com.cmc.sparky.common.dto.AcTokenResponse;
import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.dto.TokenDto;
import com.cmc.sparky.common.exception.ErrorCode;
import com.cmc.sparky.common.exception.UnauthorizedException;
import com.cmc.sparky.common.repository.TokenRepository;
import com.cmc.sparky.user.domain.User;
import com.cmc.sparky.user.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.token.secret.access}")
    private String ac_secret_key;
    @Value("${jwt.token.secret.refresh}")
    private String re_secret_key;
    private Long accessExpired= Duration.ofMinutes(30).toMillis(); //30 minutes
    private Long refreshExpired=Duration.ofDays(30).toMillis(); //30 days
    private final TokenRepository tokenRepository;
    private final AccountRepository accountRepository;
    private ServerResponse serverResponse =new ServerResponse();

    public String createJsonWebToken(Map<String, Object> headers, Map<String, Object> payloads,
                                   Long expired, String secret_key){
        Date now = new Date();
        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+expired))
                .signWith(SignatureAlgorithm.HS256,secret_key)
                .compact();
    }
    public TokenDto createToken(User user) {
        Map<String, Object> headers=new HashMap<>();
        headers.put("typ","JWT");
        headers.put("alg","HS256");
        Account account=accountRepository.findByUser(user);
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("Key", user.getId());
        payloads.put("Email",account.getEmail());

        String refresh=createJsonWebToken(headers,payloads,refreshExpired,re_secret_key);
        String access=createJsonWebToken(headers,payloads,accessExpired,ac_secret_key);
        tokenRepository.save(new Token(account.getEmail(),refresh));
        return new TokenDto(refresh,access);
    }
    public String getUser(String token){
        return Jwts.parser().setSigningKey(ac_secret_key)
                .parseClaimsJws(token)
                .getBody()
                .get("Email").toString();
    }
    public Long getUserId(String token){
        return Long.parseLong(Jwts.parser().setSigningKey(ac_secret_key)
                .parseClaimsJws(token)
                .getBody()
                .get("Key").toString());
    }
    public void validateToken(String token){
        try {
            Jwts.parser().setSigningKey(ac_secret_key).parseClaimsJws(token);
        } catch (Exception e){
            throw new UnauthorizedException(ErrorCode.EXPIRE_TOKEN);
        }
    }

    public ServerResponse reissueAcToken(String token){
        try {
            Jwts.parser().setSigningKey(re_secret_key).parseClaimsJws(token);
            Long id=Long.parseLong(Jwts.parser().setSigningKey(re_secret_key)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("Key").toString());
            String email=Jwts.parser().setSigningKey(re_secret_key)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("Email").toString();

            Map<String, Object> headers=new HashMap<>();
            headers.put("typ","JWT");
            headers.put("alg","HS256");

            Map<String, Object> payloads = new HashMap<>();
            payloads.put("Key",id);
            payloads.put("Email",email);
            AcTokenResponse acTokenResponse=new AcTokenResponse(createJsonWebToken(headers,payloads,
                    accessExpired,ac_secret_key));
            return serverResponse.success("새로 발급된 access token 입니다.",acTokenResponse);
        } catch (Exception e){
            throw new UnauthorizedException(ErrorCode.EXPIRE_TOKEN);
        }
    }
}