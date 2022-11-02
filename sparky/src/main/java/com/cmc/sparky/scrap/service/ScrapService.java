package com.cmc.sparky.scrap.service;

import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.exception.ConflictException;
import com.cmc.sparky.common.exception.ErrorCode;
import com.cmc.sparky.common.exception.NotFoundException;
import com.cmc.sparky.scrap.domain.Scrap;
import com.cmc.sparky.scrap.domain.ScrapMap;
import com.cmc.sparky.scrap.domain.Tag;
import com.cmc.sparky.scrap.dto.*;
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
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final TagRepository tagRepository;
    private final ScrapMapRepository scrapMapRepository;
    private final UserRepository userRepository;
    private ServerResponse serverResponse =new ServerResponse();
    public void saveMapping(Scrap scrap, List<Long> tags){
        List<ScrapMap> scrapMaps=new ArrayList<>();
        for(Long tag:tags){
            System.out.println(tag);
            ScrapMap scrapMap=new ScrapMap();
            scrapMap.setUser(scrap.getUser()); // 여러번 찾기 귀찮아서 일단 유저를 매핑
            scrapMap.setScrap(scrap);
            Tag tag_entity=tagRepository.findById(tag).orElse(null);
            if(tag_entity==null){
                throw new NotFoundException(ErrorCode.INVALID_TAG);
            }
            scrapMap.setTag(tag_entity);
            scrapMap.setPostDate(LocalDateTime.now());
            scrapMaps.add(scrapMap);
        }
        for(ScrapMap scrapMap: scrapMaps){
            scrapMapRepository.save(scrapMap);
        }
    }
    public void deleteMapping(Scrap scrap){
        List<ScrapMap> scrapMaps=scrapMapRepository.findAllByScrap(scrap);
        for(ScrapMap scrapMap : scrapMaps){
            scrapMapRepository.delete(scrapMap);
        }
    }

    public ServerResponse saveScrap(User user, ScrapRequest scrapRequest){
        Scrap scrap=new Scrap();
        scrap.setUser(user);
        scrap.setTitle(scrapRequest.getTitle());
        scrap.setSubTitle(scrapRequest.getSubTitle());
        scrap.setMemo(scrapRequest.getMemo());
        scrap.setImgUrl(scrapRequest.getImgUrl());
        scrap.setScpUrl(scrapRequest.getScpUrl());
        scrap.setPostDate(LocalDateTime.now());
        scrapRepository.save(scrap);
        if(scrapRequest.getTags()!=null) saveMapping(scrap,scrapRequest.getTags());
        SaveResponse saveResponse=new SaveResponse();
        saveResponse.setScrapId(scrap.getId());
        return serverResponse.success("스크랩을 저장했습니다.",saveResponse);
    }
    //수정필요
    public ServerResponse updateScrap(Long scrapId, ScrapRequest scrapRequest){
        Scrap scrap= scrapRepository.findById(scrapId).orElse(null);
        scrap.setTitle(scrapRequest.getTitle());
        scrap.setMemo(scrapRequest.getMemo());
        scrap.setImgUrl(scrapRequest.getImgUrl());
        scrap.setScpUrl(scrapRequest.getScpUrl());
        scrap.setPostDate(LocalDateTime.now());
        scrapRepository.save(scrap);

        deleteMapping(scrap);
        if(scrapRequest.getTags()!=null) saveMapping(scrap,scrapRequest.getTags());
        return serverResponse.success("스크랩을 수정했습니다.");
    }
    public ServerResponse deleteScrap(Long scrapId){
        Scrap scrap=scrapRepository.findById(scrapId).orElse(null);
        scrap.setUsed(0);
        scrapRepository.save(scrap);
        return serverResponse.success("스크랩을 삭제했습니다.");
    }
    public ServerResponse saveTag(Long uid,TagRequest tagRequest){
        Tag tag = new Tag();
        User user=userRepository.findById(uid).orElse(null);
        if(tagRepository.findByNameAndUser(tagRequest.getTag(),user)!=null){
            throw new ConflictException(ErrorCode.DUPLICATE_TAG);
        }
        tag.setName(tagRequest.getTag());
        tag.setColor(tagRequest.getColor());
        tag.setUser(user);
        tagRepository.save(tag);
        TagResponse tagResponse=new TagResponse(tag.getId(),tag.getName(),tag.getColor());
        return serverResponse.success("태그를 성공적으로 추가했습니다.",tagResponse);
    }
    public ServerResponse loadTags(Long uid){
        User user=userRepository.findById(uid).orElse(null);
        List<Tag> tags=tagRepository.findAllByUserOrderByIdDesc(user);
        List<TagResponse> tagsResponses=new ArrayList<>();
        for (Tag tag: tags){
            tagsResponses.add(new TagResponse(tag.getId(),tag.getName(),tag.getColor()));
        }
        TagsResponse tagsResponse=new TagsResponse(tagsResponses);
        return serverResponse.success("최근 사용한 태그 목록을 출력합니다.", tagsResponse);
    }
    public List<ScrapResponse> findScrap(Long uid, Integer size, Long other){
        User user=userRepository.findById(uid).orElse(null);
        User another=null;
        Pageable pageable = PageRequest.of(0,size, Sort.by("postDate").descending());
        Page<Scrap> pages=scrapRepository.findByUserAndUsed(user, 1, pageable);
        List<Scrap> scraps=pages.getContent();
        List<ScrapResponse> scrapResponses=new ArrayList<>();
        List<String> tagsName=new ArrayList<>();
        List<Tag> tags=new ArrayList<>();

        if(other!=0L) {
            another = userRepository.findById(other).orElse(null);
            tagsName = tagRepository.findNameByUser(another);
            tags = tagRepository.findAllByUser(another);
        }
        for (Scrap scrap : scraps){
            List<ScrapMap> scrapMaps= scrapMapRepository.findAllByScrap(scrap);
            List<TagResponse> tagResponses=new ArrayList<>();
            for (ScrapMap scrapMap: scrapMaps){
                Tag tag=new Tag();
                if(other!=0L){
                    Integer index=tagsName.indexOf(scrapMap.getTag().getName());
                    if(index!=-1) tag=tags.get(index);
                    else tag=scrapMap.getTag();
                }
                else{tag=scrapMap.getTag();}
                tagResponses.add(new TagResponse(tag.getId(),tag.getName(),tag.getColor()));
            }
            scrapResponses.add(new ScrapResponse(scrap.getId(), scrap.getTitle(),scrap.getSubTitle(),scrap.getMemo(),
                    scrap.getImgUrl(),scrap.getScpUrl(),tagResponses));
        }


        return scrapResponses;
    }
    public ServerResponse loadScraps(Long uid, Integer type){
        HomeResponse homeResponse=new HomeResponse();
        if(type==1){
            homeResponse.setMyScraps(findScrap(uid,100,0L));
        }
        else {
            homeResponse.setMyScraps(findScrap(uid, 5,0L));
            homeResponse.setRecScraps(findScrap(1L,5,uid));
        }
        return serverResponse.success("[홈] 스크랩을 불러옵니다.",homeResponse);
    }

    public ServerResponse searchScraps(Long uid,SearchRequest searchRequest){
        User user=userRepository.findById(uid).orElse(null);
        String find_title="%"+searchRequest.getTitle()+"%";
        List<Scrap> scraps=new ArrayList<>();
        if(searchRequest.getType()==0) scraps=scrapRepository.findAllByTitleLikeAndUserAndUsedNotOrderByPostDateDesc(find_title, user, 1);
        else scraps=scrapRepository.findAllByTitleLikeAndUserAndUsedOrderByPostDateDesc(find_title, user, 1);
        List<ScrapResponse> scrapResponses=new ArrayList<>();
        for (Scrap scrap : scraps){
            Integer cnt=0;
            List<ScrapMap> scrapMaps= scrapMapRepository.findAllByScrap(scrap);
            List<TagResponse> tagResponses=new ArrayList<>();
            for (ScrapMap scrapMap: scrapMaps){
                Tag tag=new Tag();
                if(searchRequest.getType()==0){
                    Tag myTag=tagRepository.findByNameAndUser(scrapMap.getTag().getName(),user);
                    if(myTag==null)tag=scrapMap.getTag();
                    else tag=myTag;
                }
                else{tag=scrapMap.getTag();}

                if(searchRequest.getType()==1 && searchRequest.getTags().indexOf(tag.getId())!=-1){
                    cnt++;
                }

                tagResponses.add(new TagResponse(tag.getId(),tag.getName(),tag.getColor()));
            }
            if(cnt==searchRequest.getTags().size() || searchRequest.getType()==0) {
                scrapResponses.add(new ScrapResponse(scrap.getId(), scrap.getTitle(), scrap.getSubTitle(), scrap.getMemo(),
                        scrap.getImgUrl(), scrap.getScpUrl(), tagResponses));
            }
        }
        return serverResponse.success("검색에 성공했습니다.",scrapResponses);
    }


}
