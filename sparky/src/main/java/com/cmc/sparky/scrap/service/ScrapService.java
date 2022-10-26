package com.cmc.sparky.scrap.service;

import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.scrap.domain.Scrap;
import com.cmc.sparky.scrap.domain.ScrapMap;
import com.cmc.sparky.scrap.domain.Tag;
import com.cmc.sparky.scrap.dto.*;
import com.cmc.sparky.scrap.exception.DupTagException;
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
    private ServerResponse serverResponse =new ServerResponse();
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
        saveMapping(scrap,scrapRequest.getTags());

        SaveResponse saveResponse=new SaveResponse();
        saveResponse.setScrapId(scrap.getId());
        serverResponse.setMessage("스크랩을 저장했습니다.");
        serverResponse.setResult(saveResponse);
        serverResponse.setResult(null);
        return serverResponse;
    }
    //수정필요
    public ServerResponse updateScrap(ScrapRequest scrapRequest){
        Scrap scrap= new Scrap();
        scrap.setTitle(scrapRequest.getTitle());
        scrap.setMemo(scrapRequest.getMemo());
        scrap.setImgUrl(scrapRequest.getImgUrl());
        scrap.setScpUrl(scrapRequest.getScpUrl());
        scrap.setPostDate(LocalDateTime.now());
        scrapRepository.save(scrap);
        saveMapping(scrap,scrapRequest.getTags());

        SaveResponse saveResponse=new SaveResponse();
        saveResponse.setScrapId(scrap.getId());
        serverResponse.setMessage("스크랩을 수정했습니다.");
        serverResponse.setResult(null);
        return serverResponse;
    }
    public ServerResponse deleteScrap(Long scrapId){
        Scrap scrap=scrapRepository.findById(scrapId).orElse(null);
        scrap.setUsed(0);
        scrapRepository.save(scrap);
        serverResponse.setMessage("스크랩을 삭제했습니다.");
        serverResponse.setResult(null);
        return serverResponse;
    }
    public ServerResponse saveTag(Long uid,TagRequest tagRequest){
        Tag tag = new Tag();
        User user=userRepository.findById(uid).orElse(null);
        if(tagRepository.findByNameAndUser(tagRequest.getTag(),user)!=null){
            throw new DupTagException("이미 존재하는 태그입니다.");
        }
        tag.setName(tagRequest.getTag());
        tag.setColor(tagRequest.getColor());
        tag.setUser(user);
        tagRepository.save(tag);
        serverResponse.setCode("0000");
        serverResponse.setMessage("태그를 성공적으로 추가했습니다.");
        serverResponse.setResult(new TagResponse(tag.getId(),tag.getName(),tag.getColor()));
        return serverResponse;
    }
    public ServerResponse loadTags(Long uid){
        User user=userRepository.findById(uid).orElse(null);
        List<Tag> tags=tagRepository.findAllByUserOrderByIdDesc(user);
        List<TagResponse> tagsResponses=new ArrayList<>();
        for (Tag tag: tags){
            tagsResponses.add(new TagResponse(tag.getId(),tag.getName(),tag.getColor()));
        }
        serverResponse.setCode("0000");
        serverResponse.setMessage("최근 사용한 태그 목록을 출력합니다.");
        serverResponse.setResult(new TagsResponse(tagsResponses));
        return serverResponse;
    }
    public List<ScrapResponse> findScrap(Long uid, Integer size, Long other){
        User user=userRepository.findById(uid).orElse(null);
        Pageable pageable = PageRequest.of(0,size, Sort.by("postDate").descending());
        Page<Scrap> pages=scrapRepository.findByUserAndUsed(user, 1, pageable);
        List<Scrap> scraps=pages.getContent();
        List<ScrapResponse> scrapResponses=new ArrayList<>();
        for (Scrap scrap : scraps){
            List<ScrapMap> scrapMaps= scrapMapRepository.findAllByScrap(scrap);
            List<TagResponse> tagResponses=new ArrayList<>();
            for (ScrapMap scrapMap: scrapMaps){
                Tag tag=new Tag();
                if(other!=0L){
                    User mine=userRepository.findById(other).orElse(null);
                    Tag myTag=tagRepository.findByNameAndUser(scrapMap.getTag().getName(),mine);
                    if(myTag==null)tag=scrapMap.getTag();
                    else tag=myTag;
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
        serverResponse.setCode("0000");
        serverResponse.setMessage("스크랩 로드에 성공했습니다.");
        serverResponse.setResult(homeResponse);
        return serverResponse;
    }

    public ServerResponse searchScraps(Long uid,SearchRequest searchRequest){
        User user=userRepository.findById(uid).orElse(null);
        String find_title="%"+searchRequest.getTitle()+"%";
        List<Scrap> scraps=new ArrayList<>();
        if(searchRequest.getType()==0) scraps=scrapRepository.findAllByTitleLikeAndUserNotOrderByPostDateDesc(find_title, user);
        else scraps=scrapRepository.findAllByTitleLikeAndUserOrderByPostDateDesc(find_title, user);
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
        serverResponse.setCode("0000");
        serverResponse.setMessage("검색에 성공했습니다.");
        serverResponse.setResult(scrapResponses);
        return serverResponse;
    }


}
