package com.cmc.sparky.account.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "Mail", timeToLive = 180)

public class Mail {
    @Id
    private String email;
    private String number;
    public Mail(String email, String number){
        this.email=email;
        this.number=number;
    }
}
