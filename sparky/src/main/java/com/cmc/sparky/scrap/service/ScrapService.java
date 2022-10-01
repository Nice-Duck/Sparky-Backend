package com.cmc.sparky.scrap.service;

import com.cmc.sparky.scrap.domain.Scrap;
import com.cmc.sparky.scrap.domain.ScrapMap;
import com.cmc.sparky.scrap.domain.Tag;
import com.cmc.sparky.scrap.dto.ScrapRequest;
import com.cmc.sparky.scrap.dto.TagRequest;
import com.cmc.sparky.scrap.dto.TagIdResponse;
import com.cmc.sparky.scrap.dto.TagsResponse;
import com.cmc.sparky.scrap.exception.NoneTagException;
import com.cmc.sparky.scrap.repository.ScrapMapRepository;
import com.cmc.sparky.scrap.repository.ScrapRepository;
import com.cmc.sparky.scrap.repository.TagRepository;
import com.cmc.sparky.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final TagRepository tagRepository;
    private final ScrapMapRepository scrapMapRepository;
    public void saveMapping(Scrap scrap, List<Long> tags){
        for(Long tag:tags){
            ScrapMap scrapMap=new ScrapMap();
            scrapMap.setScrap(scrap);
            scrapMap.setTag(tagRepository.findById(tag).orElse(null));
            scrapMap.setPostDate(LocalDateTime.now());
            scrapMapRepository.save(scrapMap);
        }
    }
    public void saveScrap(User user, ScrapRequest scrapRequest){
        Scrap scrap=new Scrap();
        scrap.setUser(user);
        scrap.setTitle(scrapRequest.getTitle());
        scrap.setMemo(scrapRequest.getMemo());
        scrap.setImgUrl(scrapRequest.getImgUrl());
        scrap.setScpUrl(scrapRequest.getScpUrl());
        scrap.setPostDate(LocalDateTime.now());
        scrapRepository.save(scrap);
        saveMapping(scrap,scrapRequest.getTags());
    }
    public TagIdResponse saveTag(TagRequest tagRequest){
        Tag tag=new Tag();
        tag.setName(tagRequest.getTag());
        tag.setColor(tagRequest.getColor());
        tagRepository.save(tag);
        return new TagIdResponse(tag.getId());
    }
    public TagIdResponse loadTag(String name){
        Tag tag=tagRepository.findByName(name);
        if(tag==null){
            throw new NoneTagException("태그가 존재하지 않습니다.");
        }
       return new TagIdResponse(tag.getId());
    }
}
