package com.newtranx.cloud.edit.controller;

import com.alibaba.fastjson.JSONArray;
import com.newtranx.cloud.edit.common.entities.Result;
import com.newtranx.cloud.edit.common.util.HttpUtil;
import com.newtranx.cloud.edit.dto.ContentDto;
import com.newtranx.cloud.edit.dto.Item;
import com.newtranx.cloud.edit.dto.PersonTriples;
import com.newtranx.cloud.edit.entities.*;
import com.newtranx.cloud.edit.mongoDao.ContentMongoDao;
import com.newtranx.cloud.edit.mongoDao.DocMongoDao;
import com.newtranx.cloud.edit.mongoDao.PersonTriplesMongoDao;
import com.newtranx.cloud.edit.service.ContentIndexService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @Author: niujiaxin
 * @Date: 2021-12-19 17:18
 */

@RestController
@Slf4j
@RequestMapping("/docParse")
public class ParseTextController {

//    String baseUrl = "C:\\develop\\IdeaProjects\\bossLee\\01181514_001\\txt\\";
    String baseUrl = "/data/myfile";

    @Resource
    private ContentMongoDao contentMongoDao;
    @Resource
    private DocMongoDao docMongoDao;
    @Resource
    private PersonTriplesMongoDao personTriplesMongoDao;
    @Resource
    private ContentIndexService contentIndexService;


    @GetMapping("/getContent")
    public Object getContent(){
        String locationPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        return locationPath;

    }
    @GetMapping("/getMulu")
    public Object getMulu(String fileId){
        String locationPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
//        docEsService.findByName("mulu");
        List<ContentEsBean> byFileId = contentMongoDao.findByFileId(fileId);
        List<ContentDto> contentDtos = JSONArray.parseArray(byFileId.get(0).getMuluJson(), ContentDto.class);
        return Result.getSuccessResult(contentDtos);

    }
    @GetMapping("/getNeirong")
    public Object getMulu(String titleParam,Integer page){
//        String getNeirong = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        List<DocEsBean> byBianzhangjie = docMongoDao.findByBianzhangjie(titleParam);
        return Result.getSuccessResult(byBianzhangjie.get(0));

    }
    @GetMapping("/AI")
    public Object linkAI(String aiParam,Integer taskid){
        List<DocEsBean> byNameOrDesc = docMongoDao.findByDescLikeAndDocId(aiParam,taskid);
        return Result.getSuccessResult(byNameOrDesc);
    }

    @GetMapping("/getAllperson")
    public Object getAllperson(Integer taskid){
        List<PersonTriples> byTaskid = personTriplesMongoDao.findByTaskid(taskid);
        return Result.getSuccessResult(byTaskid);

    }

    @GetMapping("updateAiInfo")
    public Result updateAiInfo(Integer id,String personJson,String eventJson,String geogJson,String orgInfo){
        //掉算法接口
        HttpResponse httpResponse = HttpUtil.get("");
        DocEsBean docEsBean = new DocEsBean();
        docEsBean.setDocId(id);
        docEsBean.setPerson(personJson);
        docEsBean.setEvent(eventJson);
        docEsBean.setGeographyInfo(geogJson);
        docEsBean.setOrganization(orgInfo);
        docMongoDao.save(docEsBean);
        return Result.getSuccessResult();

    }
    

}
