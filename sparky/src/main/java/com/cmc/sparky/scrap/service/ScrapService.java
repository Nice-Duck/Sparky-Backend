package com.cmc.sparky.scrap.service;

import com.cmc.sparky.scrap.domain.Scrap;
import com.cmc.sparky.scrap.dto.ScrapRequest;
import com.cmc.sparky.scrap.repository.ScrapRepository;
import com.cmc.sparky.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    public void saveScrap(User user, ScrapRequest scrapRequest){
        Scrap scrap=new Scrap();
        scrap.setUser(user);
        scrap.setTitle(scrapRequest.getTitle());
        scrap.setMemo(scrapRequest.getMemo());
        scrap.setImgUrl(scrapRequest.getImgUrl());
        scrap.setScpUrl(scrapRequest.getScpUrl());
        scrap.setPostDate(LocalDateTime.now());
        scrapRepository.save(scrap);
    }
}
