package com.cmc.sparky.scrap.service;

import com.cmc.sparky.common.dto.SuccessResponse;
import com.cmc.sparky.scrap.domain.Scrap;
import com.cmc.sparky.scrap.domain.ScrapMap;
import com.cmc.sparky.scrap.domain.Tag;
import com.cmc.sparky.scrap.dto.ScrapRequest;
import com.cmc.sparky.scrap.dto.TagRequest;
import com.cmc.sparky.scrap.dto.TagResponse;
import com.cmc.sparky.scrap.dto.TagsResponse;
import com.cmc.sparky.scrap.repository.ScrapMapRepository;
import com.cmc.sparky.scrap.repository.ScrapRepository;
import com.cmc.sparky.scrap.repository.TagRepository;
import com.cmc.sparky.user.domain.User;
import com.cmc.sparky.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final TagRepository tagRepository;
    private final ScrapMapRepository scrapMapRepository;
    private final UserRepository userRepository;
    public void saveMapping(Scrap scrap, List<Long> tags){
        for(Long tag:tags){
            ScrapMap scrapMap=new ScrapMap();
            scrapMap.setUser(scrap.getUser()); // 여러번 찾기 귀찮아서 일단 유저를 매핑
            scrapMap.setScrap(scrap);
            scrapMap.setTag(tagRepository.findById(tag).orElse(null));
            scrapMap.setPostDate(LocalDateTime.now());
            scrapMapRepository.save(scrapMap);
        }
    }
    public SuccessResponse saveScrap(User user, ScrapRequest scrapRequest){
        Scrap scrap=new Scrap();
        scrap.setUser(user);
        scrap.setTitle(scrapRequest.getTitle());
        scrap.setMemo(scrapRequest.getMemo());
        scrap.setImgUrl(scrapRequest.getImgUrl());
        scrap.setScpUrl(scrapRequest.getScpUrl());
        scrap.setPostDate(LocalDateTime.now());
        scrapRepository.save(scrap);
        saveMapping(scrap,scrapRequest.getTags());

        SuccessResponse successResponse=new SuccessResponse();
        successResponse.setCode("0000");
        successResponse.setMessage("스크랩을 저장했습니다.");
        successResponse.setResult(null);
        return successResponse;
    }
    public SuccessResponse saveTag(TagRequest tagRequest){
        SuccessResponse successResponse=new SuccessResponse();
        if(tagRepository.findByName(tagRequest.getTag())!=null){
            successResponse.setCode("1001");
            successResponse.setMessage("이미 존재하는 태그입니다.");
            successResponse.setResult(null);
        }
        else {
            Tag tag = new Tag();
            tag.setName(tagRequest.getTag());
            tag.setColor(tagRequest.getColor());
            tagRepository.save(tag);
            successResponse.setCode("0000");
            successResponse.setMessage("태그를 성공적으로 추가했습니다.");
            successResponse.setResult(new TagResponse(tag.getId(),tag.getName(),tag.getColor()));
        }
        return successResponse;
    }
    public SuccessResponse loadTag(String name){
        Tag tag=tagRepository.findByName(name);
        SuccessResponse successResponse=new SuccessResponse();
        if(tag==null){
            successResponse.setCode("1000");
            successResponse.setMessage("태그가 존재하지 않습니다.");
            successResponse.setResult(null);
        }
        else{
            successResponse.setCode("0000");
            successResponse.setMessage("태그가 존재합니다.");
            successResponse.setResult(new TagResponse(tag.getId(),tag.getName(),tag.getColor()));
        }
       return successResponse;
    }
    public SuccessResponse loadLastTags(Long uid){
        SuccessResponse successResponse=new SuccessResponse();
        User user=userRepository.findById(uid).orElse(null);
        Pageable pageable = PageRequest.of(0,10, Sort.by("postDate").descending());
        Page<ScrapMap> pages=scrapMapRepository.findAllByUser(user,pageable);
        List<ScrapMap> scrapMaps=pages.getContent();
        List<TagResponse> tagsResponses=new ArrayList<>();
        List<Long> tagDuplicate=new ArrayList<>();
        for (ScrapMap scrapMap : scrapMaps){
            Tag tag=scrapMap.getTag();
            if(tagDuplicate.contains(tag.getId()))continue;
            tagDuplicate.add(tag.getId());
            tagsResponses.add(new TagResponse(tag.getId(),tag.getName(),tag.getColor()));
        }
        successResponse.setCode("0000");
        successResponse.setMessage("최근 사용한 태그 목록을 출력합니다.");
        successResponse.setResult(new TagsResponse(tagsResponses));
        return successResponse;
    }
}
