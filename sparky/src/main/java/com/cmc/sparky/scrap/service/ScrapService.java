package com.cmc.sparky.scrap.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cmc.sparky.common.dto.ServerResponse;
import com.cmc.sparky.common.exception.ConflictException;
import com.cmc.sparky.common.exception.ErrorCode;
import com.cmc.sparky.common.exception.NotFoundException;
import com.cmc.sparky.scrap.domain.Declaration;
import com.cmc.sparky.scrap.domain.Scrap;
import com.cmc.sparky.scrap.domain.ScrapMap;
import com.cmc.sparky.scrap.domain.Tag;
import com.cmc.sparky.scrap.dto.*;
import com.cmc.sparky.scrap.repository.DeclarationRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private String S3Bucket = "sparkyapp"; // Bucket 이름
    private final AmazonS3Client amazonS3Client;
    private final ScrapRepository scrapRepository;
    private final TagRepository tagRepository;
    private final ScrapMapRepository scrapMapRepository;
    private final UserRepository userRepository;
    private final DeclarationRepository declarationRepository;
    private ServerResponse serverResponse =new ServerResponse();
    public ServerResponse urlValidator(String url){
        try {
            URL urlCheck = new URL(url);
            URLConnection connection=urlCheck.openConnection();
            HttpURLConnection exitCode = (HttpURLConnection)connection;
            System.out.println(exitCode.getResponseCode());
            return serverResponse.success("존재하는 URL 입니다.");
        }catch(Exception e){
            throw new NotFoundException(ErrorCode.INVALID_URL);
        }

    }
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
    public ServerResponse updateScrap(Long scrapId, UpdateRequest updateRequest) throws Exception {
        Scrap scrap= scrapRepository.findById(scrapId).orElse(null);
        scrap.setTitle(updateRequest.getTitle());
        scrap.setSubTitle(updateRequest.getSubTitle());
        scrap.setMemo(updateRequest.getMemo());
        scrap.setPostDate(LocalDateTime.now());

        if(updateRequest.getImage().getSize()!=0){
            MultipartFile multipartFile=updateRequest.getImage();
            String originalName = multipartFile.getOriginalFilename(); // 파일 이름
            long size = multipartFile.getSize(); // 파일 크기
            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(size);
            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );
            String imagePath = amazonS3Client.getUrl(S3Bucket, originalName).toString(); // 접근가능한 URL 가져오기
            scrap.setImgUrl(imagePath);
        }

        scrapRepository.save(scrap);

        deleteMapping(scrap);
        if(updateRequest.getTags()!=null) saveMapping(scrap,updateRequest.getTags());
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
        List<Tag> tags=tagRepository.findAllByUserAndIsDeletedOrderByIdDesc(user,false);
        List<TagResponse> tagsResponses=new ArrayList<>();
        for (Tag tag: tags){
            tagsResponses.add(new TagResponse(tag.getId(),tag.getName(),tag.getColor()));
        }
        TagsResponse tagsResponse=new TagsResponse(tagsResponses);
        return serverResponse.success("최신순으로 모든 태그를 출력합니다.", tagsResponse);
    }
    public List<ScrapResponse> findScrap(Long uid, Integer size, Long other, Integer type){
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
            scrapResponses.add(new ScrapResponse(type, scrap.getId(), scrap.getTitle(),scrap.getSubTitle(),scrap.getMemo(),
                    scrap.getImgUrl(),scrap.getScpUrl(),tagResponses));
        }


        return scrapResponses;
    }
    public ServerResponse loadScraps(Long uid, Integer type){
        HomeResponse homeResponse=new HomeResponse();
        if(type==1){
            homeResponse.setMyScraps(findScrap(uid,100,0L, 1));
        }
        else {
            homeResponse.setMyScraps(findScrap(uid, 5,0L,1));
            homeResponse.setRecScraps(findScrap(1L,5,uid,0));
        }
        return serverResponse.success("[홈] 스크랩을 불러옵니다.",homeResponse);
    }

    public ServerResponse searchScraps(Long uid,SearchRequest searchRequest){
        User user=userRepository.findById(uid).orElse(null);
        String find_title="%"+searchRequest.getTitle()+"%";
        List<Scrap> scraps=new ArrayList<>();
        if(searchRequest.getType()==0) scraps=scrapRepository.findAllByTitleLikeAndUserNotAndUsedOrderByPostDateDesc(find_title, user, 1);
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
                scrapResponses.add(new ScrapResponse(searchRequest.getType(),scrap.getId(), scrap.getTitle(), scrap.getSubTitle(), scrap.getMemo(),
                        scrap.getImgUrl(), scrap.getScpUrl(), tagResponses));
            }
        }
        return serverResponse.success("검색에 성공했습니다.",scrapResponses);
    }
    public ServerResponse declareScraps(Long uid,Long scrapId) {
        Scrap scrap=scrapRepository.findById(scrapId).orElse(null);
        User user=userRepository.findById(uid).orElse(null);
        if(declarationRepository.findByScrapAndUser(scrap,user)!=null)
            throw new ConflictException(ErrorCode.DUPLICATE_DECLARE);
        Long cnt= declarationRepository.countByScrap(scrap);
        System.out.println(cnt);
        if(cnt>=3){scrap.setUsed(0);}
        else{
            Declaration declaration=new Declaration();
            declaration.setScrap(scrap);
            declaration.setUser(user);
            declarationRepository.save(declaration);
        }
        return serverResponse.success("신고 완료되었습니다.");
    }
    public ServerResponse updateTag(Long uid,TagUpdateRequest tagUpdateRequest){
        User user=userRepository.findById(uid).orElse(null);
        Tag tag=tagRepository.findById(tagUpdateRequest.getTagId()).orElse(null);
        if(tagRepository.findByNameAndUser(tagUpdateRequest.getName(),user)!=null){
            throw new ConflictException(ErrorCode.DUPLICATE_TAG);
        }
        tag.setName(tagUpdateRequest.getName());
        tagRepository.save(tag);
        TagResponse tagResponse=new TagResponse(tag.getId(),tag.getName(),tag.getColor());
        return serverResponse.success("태그를 성공적으로 수정했습니다.",tagResponse);
    }
    public ServerResponse deleteTag(Long tid){
        Tag tag=tagRepository.findById(tid).orElse(null);
        tag.setIsDeleted(true);
        List<ScrapMap> scrapMaps=scrapMapRepository.findAllByTag(tag);
        List<Scrap> scraps=new ArrayList<>();
        for(ScrapMap scrapMap : scrapMaps){
            scraps.add(scrapMap.getScrap());
            scrapMapRepository.delete(scrapMap);
        }
        for(Scrap scrap: scraps){
            List<ScrapMap> scrapTemp=scrapMapRepository.findAllByScrap(scrap);
            if(scrapTemp.size()==0){
                scrap.setUsed(0);
                scrapRepository.save(scrap);
            }
        }
        return serverResponse.success("태그를 성공적으로 삭제했습니다.");
    }

}
