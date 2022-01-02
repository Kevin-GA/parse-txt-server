package com.newtranx.cloud.edit.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.newtranx.cloud.edit.common.entities.Result;
import com.newtranx.cloud.edit.common.util.EntityChangeUtil;
import com.newtranx.cloud.edit.common.util.FileUtils;
import com.newtranx.cloud.edit.common.util.HttpUtil;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Resource
    private DocEsService docEsService;
    @Resource
    private ContentEsService contentEsService;
    @Resource
    private ContentIndexService contentIndexService;

    private int matchTime = 0;
    //            Map<String, Object> preMap = new HashMap<>();
    private Map<String, String> bianMap = new HashMap<>();
    private Map<String, String> zhangMap = new HashMap<>();
    private Map<String, String> jieMap = new HashMap<>();

    @GetMapping("/getContent")
    public Object getContent(){
        String locationPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        return locationPath;

    }
    @GetMapping("/getMulu")
    public Object getMulu(){
        String locationPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
//        docEsService.findByName("mulu");
        return init("");

    }
    @GetMapping("/getNeirong")
    public Object getMulu(String titleParam,Integer page){
        String getNeirong = ClassUtils.getDefaultClassLoader().getResource("").getPath();
//        docEsService.findByNameOrDesc("pianzhangjie","");
        return Result.getSuccessResult("范志民男，汉族，1921年2月出生，五莲县院西乡范家车村人。系中国美术协会会员、获首届邹韬奋出版奖的全国十大美术编辑之一。曾编辑出版《中国画家丛书》20余种，中国美术史论10余种，并任《中国美术全集》中《古代版画》的副主编，编纂大型专业工具书《中国美术家名人辞典》、《古代画汇览》，其版画、国画、水粉画作品多次选入各级展览。");

    }

    @GetMapping("/init")
    public Result<Object> init(String filePath){
        String locationPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        List<File> txtList = FileUtils.getFilesFromFolder(locationPath);
        List<File> muluList = txtList.stream().filter(a -> a.getName().startsWith("C")).collect(Collectors.toList());
        List<File> contentList = txtList.stream().filter(a -> a.getName().startsWith("T")).collect(Collectors.toList());
        List<ContentIndexEntity> contentIndexList = new ArrayList<>();
//        todo 1、读取目录文件生成目录结构对象
        for (File f : muluList) {
            readContentFileByLines(baseUrl + f.getName(),contentIndexList);

        }

        List<ContentIndex> contentLists = null;
        try {
            contentLists = EntityChangeUtil.toDTO(ContentIndex.class, contentIndexList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("实体转换异常");
        }
        //存储目录结构
//        contentIndexService.saveBatch(contentLists);

        List<ContentDto> contentDtos = new ArrayList<>();
        List<ContentIndexEntity> JieList = contentIndexList
                .stream()
                .filter(o -> o.getJie()!=null&&o.getJie().length()>0)
                .filter(o -> o.getJie().matches("^(\\u7b2c).+(\\u8282)"))//节匹配
                .collect(Collectors.toList());
        System.out.println(JieList);

        List<String> bianStrs = new ArrayList<>();
        for (ContentIndexEntity c:JieList) {
            bianStrs.add(c.getBian());
        }
        //获取唯一编集合
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
                        subSub.setPage(contentIndexEntity.getPageNum());
                        jieChild.add(subSub);
                    }

                }
                sub.setChild(jieChild);
                zhangChild.add(sub);

            }
            contentDto.setChild(zhangChild);
        }

        //mulu存储到es
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
        //掉算法接口
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
    
    public void readContentFileByLines(String fileName, List<ContentIndexEntity> contentIndexEntities) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取目录文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                subContentIndexEntity.set
//                ^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$
//                第的unicode
//                ^(\u7b2c)(\u4e00|\u4e8c|\u4e09|\u56db|\u4e94|\u516d|\u4e03|\u516b|\u4e5d|\u5341)(\u5341)?(\u4e00|\u4e8c|\u4e09|\u56db|\u4e94|\u516d|\u4e03|\u516b|\u4e5d)?(\u7f16)$
//
//^(\u7b2c)(\u4e00|\u4e8c|\u4e09|\u56db|\u4e94|\u516d|\u4e03|\u516b|\u4e5d|\u5341)(\u5341)?(\u4e00|\u4e8c|\u4e09|\u56db|\u4e94|\u516d|\u4e03|\u516b|\u4e5d)
//                String patternStr = "^(\\u7b2c)(\\u4e00|\\u4e8c|\\u4e09|\\u56db|\\u4e94|\\u516d|\\u4e03|\\u516b|\\u4e5d|\\u5341)(\\u5341)?(\\u4e00|\\u4e8c|\\u4e09|\\u56db|\\u4e94|\\u516d|\\u4e03|\\u516b|\\u4e5d)";

//           匹配章节
//                String patternStr = "^(\\u7b2c).+(\\u7f16|\\u7ae0|\\u8282)";

//                tempString ="第一节 革命历史档案.................87";
                //页首第一个char为空
                if(line==1){
                    char[] chars = tempString.toCharArray();
                    char[] newChars =new char[chars.length-1];
                    for (int i = 0; i < chars.length-1; i++) {
                        newChars[i]=chars[i+1];
                    }
                    tempString=String.valueOf(newChars);
                }

                String jiePatternStr = "^(\\u7b2c).+(\\u8282)(\\s)";
                String zhangPatternStr = "^(\\u7b2c).+(\\u7ae0)(\\s)";
                String bianPatternStr = "^(\\u7b2c).+(\\u7f16)(\\s)";
//                匹配文末数字
                String intPatternStr = "\\d*$";
                Pattern bianPattern = Pattern.compile(bianPatternStr);
                Pattern zhangPattern = Pattern.compile(zhangPatternStr);
                Pattern jiePattern = Pattern.compile(jiePatternStr);
                Pattern patternInt = Pattern.compile(intPatternStr);

                Matcher bianMatcher = bianPattern.matcher(tempString);
                Matcher zhangMatcher = zhangPattern.matcher(tempString);
                Matcher jieMatcher = jiePattern.matcher(tempString);

                Pattern prePattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
                Matcher preMatcher = prePattern.matcher(tempString);

                Matcher matcherInt = patternInt.matcher(tempString);

                ContentIndexEntity contentIndexEntity = new ContentIndexEntity();
                if (bianMatcher.find()) {
                    zhangMap.clear();
                    jieMap.clear();
                    bianMap.put("bian",bianMatcher.group().trim());

//如果匹配
//                    System.out.println(bianMatcher.group());
                    if (matcherInt.find()) {
                        System.out.println(matcherInt.group());
                        bianMap.put("bianPageNum",matcherInt.group());
//                        System.out.println(tempString.replace(bianMatcher.group(), "").replace(matcherInt.group(), "").trim().replace(".", ""));
                        bianMap.put("contentName",tempString.replace(bianMatcher.group(), "").replace(matcherInt.group(), "").trim().replace(".", ""));
                    }

                }else if (zhangMatcher.find()) {
                    jieMap.clear();
                    zhangMap.put("zhang",zhangMatcher.group().trim());
//如果匹配
//                    System.out.println("line " + line + ": " + tempString);
//                    System.out.println(bianMatcher.group()); 匹配到的字符串
                    if (matcherInt.find()) {
                        zhangMap.put("zhangPageNum",matcherInt.group());
//                        System.out.println(tempString.replace(zhangMatcher.group(), "").replace(matcherInt.group(), "").trim().replace(".", ""));
                        zhangMap.put("contentName",tempString.replace(zhangMatcher.group(), "").replace(matcherInt.group(), "").trim().replace(".", ""));
                    }

                }else if (jieMatcher.find()) {
                    jieMap.put("jie",jieMatcher.group().trim());
//如果匹配
//                    System.out.println("line " + line + ": " + tempString);
//                    System.out.println(bianMatcher.group()); 匹配到的字符串
                    if (matcherInt.find()) {
                        jieMap.put("jiePageNum",matcherInt.group());
//                        System.out.println(tempString.replace(jieMatcher.group(), "").replace(matcherInt.group(), "").trim().replace(".", ""));
                        jieMap.put("contentName",tempString.replace(jieMatcher.group(), "").replace(matcherInt.group(), "").trim().replace(".", ""));
                    }

                }else {//不是编章节就肯定是序、概述等，记录下来
                    if(preMatcher.find()){
                        bianMap.clear();
                        zhangMap.clear();
                        jieMap.clear();
                        contentIndexEntity.setPre(preMatcher.group());
                        if (matcherInt.find()&& StringUtils.isNotBlank(matcherInt.group())) {
                            System.out.println(matcherInt.group());
                            contentIndexEntity.setPageNum(Integer.parseInt(matcherInt.group()));
                        }
                    }

                }
                if (bianMap.get("bian")!=null){
                    contentIndexEntity.setBian(bianMap.get("bian"));
                    contentIndexEntity.setContentName(bianMap.get("contentName"));
                    contentIndexEntity.setPageNum(Integer.parseInt(bianMap.get("bianPageNum")));
                }
                if (zhangMap.get("zhang")!=null){
                    contentIndexEntity.setZhang(zhangMap.get("zhang"));
                    contentIndexEntity.setContentName(zhangMap.get("contentName"));
                    contentIndexEntity.setPageNum(Integer.parseInt(zhangMap.get("zhangPageNum")));
                }
                if (jieMap.get("jie")!=null){
                    contentIndexEntity.setJie(jieMap.get("jie"));
                    contentIndexEntity.setContentName(jieMap.get("contentName"));
                    contentIndexEntity.setPageNum(Integer.parseInt(jieMap.get("jiePageNum")));

                }
                contentIndexEntities.add(contentIndexEntity);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

    }

    /**
     * 获取两个章节之间的文本
     * @param fileName
     * @return
     */

    public void readSubContentFileByLines(String fileName ) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("readSubContentFileByLines以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {

                String patternStr = "^(\\u7b2c).+(\\u7f16|\\u7ae0|\\u8282)$";
                Pattern pattern = Pattern.compile(patternStr);
                Matcher matcher = pattern.matcher(tempString);
                if (matcher.find()){
                    ++matchTime;
                    System.out.println(matchTime);
                    System.out.println(tempString);
//                    tempString ="";
//                    break;
                }

                if(matchTime ==1){
                    System.out.println("line " + line + ": " + tempString);

                }
                // 显示行号
                line++;
                if(matchTime == 2)
                    return;

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

    }
}
