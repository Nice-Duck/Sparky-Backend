package com.cmc.sparky.common.service;

import com.cmc.sparky.account.domain.Account;
import com.cmc.sparky.account.repository.AccountRepository;
import com.cmc.sparky.common.domain.Token;
import com.cmc.sparky.common.dto.TokenDto;
import com.cmc.sparky.common.exception.TokenExpiredException;
import com.cmc.sparky.common.repository.TokenRepository;
import com.cmc.sparky.user.domain.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
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
    private Long accessExpired= Duration.ofDays(30).toMillis(); //30 days for test
    private Long refreshExpired=Duration.ofDays(30).toMillis(); //30 days for test
    private final TokenRepository tokenRepository;
    private final AccountRepository accountRepository;

    public TokenDto createToken(User user) {
        Map<String, Object> headers=new HashMap<>();
        headers.put("typ","JWT");
        headers.put("alg","HS256");
        Account account=accountRepository.findByUser(user);
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("Key", user.getId());
        payloads.put("Email",account.getEmail());

        Date now = new Date();
        String refresh=Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+refreshExpired))
                .signWith(SignatureAlgorithm.HS256,re_secret_key)
                .compact();
        String access= Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+accessExpired))
                .signWith(SignatureAlgorithm.HS256,ac_secret_key)
                .compact();

        tokenRepository.save(new Token(account.getEmail(),refresh));
        return new TokenDto(refresh,access);
    }
    public String getUser(String token){
        return Jwts.parser().setSigningKey(ac_secret_key)
                .parseClaimsJws(token)
                .getBody()
                .get("Email").toString();
    }
    public Long getMemberId(String token){
        return Long.parseLong(Jwts.parser().setSigningKey(ac_secret_key)
                .parseClaimsJws(token)
                .getBody()
                .get("Key").toString());
    }
    public void validateToken(String token){
        try {
            Jwts.parser().setSigningKey(ac_secret_key).parseClaimsJws(token);
        } catch (ExpiredJwtException e){
            throw new TokenExpiredException("Token has expired");
        }
    }
    /*
    public AcTokenResponse refreshCompare(String token){
        String email=Jwts.parser().setSigningKey(re_secret_key)
                .parseClaimsJws(token)
                .getBody()
                .get("Email").toString();
        Token temp=tokenRepository.findById(email).orElse(null);
        if(temp.getRefreshToken()==null){
            throw new TokenExpiredException("Token has expired");
        }
        if(!temp.getRefreshToken().equals(token)){
            throw new TokenMatchException("Invalid token");
        }
        Map<String, Object> headers=new HashMap<>();
        headers.put("typ","JWT");
        headers.put("alg","HS256");

        Login user=loginRepository.findByEmail(email);
        User member=memberRepository.findByLogin(user);

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("Key", member.getId());
        payloads.put("Email",email);
        Date now = new Date();
        return new AcTokenResponse(Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+accessExpired))
                .signWith(SignatureAlgorithm.HS256,ac_secret_key)
                .compact());
    }

     */
}