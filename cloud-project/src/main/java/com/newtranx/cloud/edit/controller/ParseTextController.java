package com.newtranx.cloud.edit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.newtranx.cloud.edit.common.entities.Result;
import com.newtranx.cloud.edit.common.util.EntityChangeUtil;
import com.newtranx.cloud.edit.common.util.FileUtils;
import com.newtranx.cloud.edit.common.util.HttpUtil;
import com.newtranx.cloud.edit.dto.ContentDto;
import com.newtranx.cloud.edit.entities.*;
import com.newtranx.cloud.edit.service.ContentEsService;
import com.newtranx.cloud.edit.service.ContentIndexService;
import com.newtranx.cloud.edit.service.DocEsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: niujiaxin
 * @Date: 2021-12-19 17:18
 */

@RestController
@Slf4j
@RequestMapping("/docParse")
public class ParseTextController {

    String baseUrl = "C:\\develop\\IdeaProjects\\bossLee\\01181514_001\\txt\\";
//    String baseUrl = "/data/myfile";

    @Resource
    private DocEsService docEsService;
    @Resource
    private ContentEsService contentEsService;
    @Resource
    private ContentIndexService contentIndexService;


    @GetMapping("/getContent")
    public Object getContent(){
        String locationPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        return locationPath;

    }
    @GetMapping("/getMulu")
    public Object getMulu(){
        String locationPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
//        docEsService.findByName("mulu");
        List<ContentEsBean> byFileId = contentEsService.findByFileId("1");
        List<ContentDto> contentDtos = JSONArray.parseArray(byFileId.get(0).getMuluJson(), ContentDto.class);
        return Result.getSuccessResult(contentDtos);

    }
    @GetMapping("/getNeirong")
    public Object getMulu(String titleParam,Integer page){
        String getNeirong = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        List<DocEsBean> byBianzhangjie = docEsService.findByBianzhangjie(titleParam);
        return Result.getSuccessResult("????????????????????????1921???2????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????20???????????????????????????10?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");

    }
    @GetMapping("/linkAI")
    public Object linkAI(String aiParam){
        List<DocEsBean> byNameOrDesc = docEsService.findByDesc(aiParam);
        return Result.getSuccessResult(byNameOrDesc);
    }

    @GetMapping("/init")
    public Result<Object> init(String filePath){
//        String locationPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        List<File> txtList = FileUtils.getFilesFromFolder(baseUrl);
        List<File> muluList = txtList.stream().filter(a -> a.getName().startsWith("C")).collect(Collectors.toList());
        List<File> contentList = txtList.stream().filter(a -> a.getName().startsWith("T")).collect(Collectors.toList());
        List<ContentIndexEntity> contentIndexList = new ArrayList<>();
//        todo 1?????????????????????????????????????????????
        for (File f : muluList) {
//            readContentFileByLines(baseUrl + f.getName(),contentIndexList);

        }

        List<ContentIndex> contentLists = null;
        try {
            contentLists = EntityChangeUtil.toDTO(ContentIndex.class, contentIndexList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("??????????????????");
        }
        //??????????????????
//        contentIndexService.saveBatch(contentLists);

        List<ContentDto> contentDtos = new ArrayList<>();
        List<ContentIndexEntity> JieList = contentIndexList
                .stream()
                .filter(o -> o.getJie()!=null&&o.getJie().length()>0)
                .filter(o -> o.getJie().matches("^(\\u7b2c).+(\\u8282)"))//?????????
                .collect(Collectors.toList());
        System.out.println(JieList);

        List<String> bianStrs = new ArrayList<>();
        for (ContentIndexEntity c:JieList) {
            bianStrs.add(c.getBian());
        }
        //?????????????????????
        List<String> bianDistinct = bianStrs.stream().distinct().collect(Collectors.toList());

        for (String c:bianDistinct) {
            ContentDto contentDto = new ContentDto();
            contentDto.setTitle(c);
//            contentDto.setPage(c.getPageNum());
            contentDtos.add(contentDto);
        }

        for (ContentDto contentDto: contentDtos) {
            List<String> zhangStrs = new ArrayList<>();
            for (ContentIndexEntity contentIndexEntity:JieList) {
                if(contentIndexEntity.getBian().equals(contentDto.getTitle())){
                    zhangStrs.add(contentIndexEntity.getZhang());
                }
            }
            List<String> zhangDistinct = zhangStrs.stream().distinct().collect(Collectors.toList());
            List<ContentDto> zhangChild = new ArrayList<>();

            for (String s:zhangDistinct) {
                ContentDto sub = new ContentDto();
                sub.setTitle(s);
                List<ContentDto> jieChild = new ArrayList<>();
                for (ContentIndexEntity contentIndexEntity:JieList) {
                    if(contentIndexEntity.getBian().equals(contentDto.getTitle()) && contentIndexEntity.getZhang().equals(s)){
                        ContentDto subSub = new ContentDto();
                        subSub.setTitle(contentIndexEntity.getJie());
                        subSub.setContentName(contentIndexEntity.getContentName());
                        subSub.setPage(contentIndexEntity.getPageNum());
                        jieChild.add(subSub);
                    }

                }
                sub.setChild(jieChild);
                zhangChild.add(sub);

            }
            contentDto.setChild(zhangChild);
        }

        //mulu?????????es
        ContentEsBean contentEsBean = new ContentEsBean();
        contentEsBean.setId(1L);
        contentEsBean.setFileId("1");
        contentEsBean.setTotalTitle("");
        contentEsBean.setMuluJson(JSON.toJSONString(contentDtos));
        contentEsService.save(contentEsBean);

        return Result.getSuccessResult(contentDtos);

    }

    @GetMapping("updateAiInfo")
    public Result updateAiInfo(Integer id,String personJson,String eventJson,String geogJson,String orgInfo){
        //???????????????
        HttpResponse httpResponse = HttpUtil.get("");
        DocEsBean docEsBean = new DocEsBean();
        docEsBean.setDocId(id);
        docEsBean.setPerson(personJson);
        docEsBean.setEvent(eventJson);
        docEsBean.setGeographyInfo(geogJson);
        docEsBean.setOrganization(orgInfo);
        docEsService.save(docEsBean);
        return Result.getSuccessResult();

    }
    

}
