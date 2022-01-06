package com.newtranx.cloud.edit.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.newtranx.cloud.edit.common.entities.Result;
import com.newtranx.cloud.edit.common.util.EntityChangeUtil;
import com.newtranx.cloud.edit.common.util.FileUtils;
import com.newtranx.cloud.edit.common.util.HttpClientUtils;
import com.newtranx.cloud.edit.dto.ContentDto;
import com.newtranx.cloud.edit.dto.Item;
import com.newtranx.cloud.edit.dto.NLPResponseDto;
import com.newtranx.cloud.edit.dto.PersonTriples;
import com.newtranx.cloud.edit.entities.*;
import com.newtranx.cloud.edit.mongoDao.ContentMongoDao;
import com.newtranx.cloud.edit.mongoDao.DocMongoDao;
import com.newtranx.cloud.edit.mongoDao.PersonTriplesMongoDao;
import com.newtranx.cloud.edit.service.ContentIndexService;
import com.newtranx.cloud.edit.service.DocJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author niujiaxin
 * @since 2021-12-27
 */
@RestController
@RequestMapping("/docJob")
@Slf4j
@Api(tags = "任务文档接口")
public class DocJobController {

    private static final Logger LOG = LoggerFactory.getLogger(DocJobController.class);

//    String baseUrl = "/data/myfile/01181514_001/txt/";
    String baseUrl = "C:\\develop\\IdeaProjects\\bossLee\\01181514_001\\txt\\";

//    String saveUrl = "/data/myfile";
    String saveUrl = "C:\\develop\\myfile\\";

    @Resource
    private DocJobService docJobService;

    @Resource
    private ContentMongoDao contentMongoDao;
    @Resource
    private DocMongoDao docMongoDao;

    @Resource
    private ContentIndexService contentIndexService;

    @Resource
    private PersonTriplesMongoDao personTriplesMongoDao;


    private int matchTime = 0;
    //            Map<String, Object> preMap = new HashMap<>();
    private Map<String, String> bianMap = new HashMap<>();
    private Map<String, String> zhangMap = new HashMap<>();
    private Map<String, String> jieMap = new HashMap<>();


    /**
     * 创建任务并上传文件
     * @param docJob
     * @param file
     * @return
     */
    @PostMapping("/create1")
    @ApiOperation(value = "创建作业，并上传文件")
    public Result<Object> create1(DocJob docJob,@RequestParam("file") MultipartFile file){
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get("UPLOADED_FOLDER" + file.getOriginalFilename());
            Files.write(path, bytes);

            LOG.info("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }
        docJobService.saveOrUpdate(docJob);
        return Result.getSuccessResult();

    }
    /**
     * 创建任务并上传文件夹下面的所有文件
     * @param docJob
     * @param file
     * @return
     */
    @PostMapping("/create")
    @ApiOperation(value = "创建作业，并上传文件")
    public Result<Object> create(DocJob docJob,String midparam,@RequestParam("file") MultipartFile[] file){
        try {
            FileUtils.saveMultiFile(saveUrl+midparam, file);

            LOG.info("message", "You successfully uploaded folder'" + file + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }
        docJob.setTaskStatus("解析中");
//        docJobService.saveOrUpdate(docJob);
        docJobService.insertReturnId(docJob);
        System.out.println(docJob.getId());
//        init(docJob.getId());
        return Result.getSuccessResult();

    }

    @GetMapping("/list")
    @ApiOperation(value = "作业列表")
    public Result<Object>  listJosbs(){
        List<DocJob> list = docJobService.list();
        return Result.getSuccessResult(list);
    }
    @GetMapping("/update")
    @ApiOperation(value = "修改作业")
    public Result<Object> update(DocJob docJob){
        return Result.getSuccessResult(docJobService.saveOrUpdate(docJob));
    }
    @GetMapping("/get")
    @ApiOperation(value = "获取作业")
    public Result<Object> update(Integer id){
        return  Result.getSuccessResult(docJobService.getById(id));
    }

    @GetMapping("/init")
    public Result<Object> init(Integer jobId){
//        String locationPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        List<File> txtList = FileUtils.getFilesFromFolder(baseUrl);
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
//        contentEsBean.setId(1L);
        contentEsBean.setFileId(jobId.toString());
        contentEsBean.setTotalTitle("");
        contentEsBean.setMuluJson(JSON.toJSONString(contentDtos));
//        contentEsService.save(contentEsBean);

        contentMongoDao.save(contentEsBean);

        //内容存储到es
        for (ContentIndexEntity contentIndexEntity:JieList) {
            StringBuffer jieContentBuffer = new StringBuffer();
            for (int i = contentIndexEntity.getPageNum(); i<= 474; i++) {
//            for (int i = 22; i<= 474; i++) {
                String resFromTxt = readSubContentFileByLines(baseUrl + formatecontentFileName(i));
                if(StringUtils.isNotBlank(resFromTxt)){
                    //文本间拼接字符
                    jieContentBuffer =  jieContentBuffer.append(new StringBuffer(resFromTxt));
                }
                if(matchTime == 2){
                    matchTime = 0;
                    break;
                }
            }
            if (matchTime == 0){
                DocEsBean docEsBean = new DocEsBean();
                docEsBean.setId(UUID.randomUUID().toString());
                docEsBean.setBianzhangjie(contentIndexEntity.getBian()+","+contentIndexEntity.getZhang()+","+contentIndexEntity.getJie());
                docEsBean.setName(contentIndexEntity.getContentName());
                docEsBean.setPage(0);
                docEsBean.setDocId(jobId);
                docEsBean.setPerson("");
                docEsBean.setEvent("");
                docEsBean.setGeographyInfo("");
                docEsBean.setOrganization("");
                docEsBean.setDesc(jieContentBuffer.toString());

//                docEsService.save(docEsBean);

                HashMap<String, String> requestBodyMap = new HashMap<>();
                if(jieContentBuffer.toString().length()>500){
                    requestBodyMap.put("text",jieContentBuffer.toString().substring(0,500));
                }else{
                    requestBodyMap.put("text",jieContentBuffer.toString());
                }

                String s = JSON.toJSONString(requestBodyMap);

                String nlpResponseStr = HttpClientUtils.postBodyJson("http://47.93.126.56:8888", JSON.toJSONString(requestBodyMap));
//                nlpResponseStr = "{\"text\":\n" +
//        "\"范志民男，汉族，1921年2月出生，五莲县院西乡范家车村人。系中国美术协会会员、获首届邹韬奋出版奖的全国十大美术编辑之一。曾编辑出版《中国画家丛书》20余种，中国美术史论10余种，并任《中国美术全集》中《古代版画》的副主编，编纂大型专业工具书《中国美术家名人辞典》、《古代画汇览》，其版画、国画、水粉画作品多次选入各级展览。\",\n" +
//        "\"items\": [{\"item\": \"范志民\", \"wordtag_label\": \"人物类_实体\", \"offset\": 0, \"length\": 3}, {\"item\": \"五莲县\", \"wordtag_label\":\n" +
//        "\"世界地区类\", \"offset\": 18, \"length\": 3}, {\"item\": \"院西乡\", \"wordtag_label\": \"世界地区类\", \"offset\": 21, \"length\": 3}, {\"item\":\n" +
//        "\"范家车村\", \"wordtag_label\": \"世界地区类\", \"offset\": 24, \"length\": 4}, {\"item\": \"中国美术协会\", \"wordtag_label\": \"组织机构类\", \"offset\": 31,\n" +
//        "\"length\": 6}, {\"item\": \"邹韬奋\", \"wordtag_label\": \"人物类_实体\", \"offset\": 43, \"length\": 3}], \"person_triples\": [{\"name\": \"范志民\",\n" +
//        "\"ethnic\": \"汉族\", \"gender\": \"男\", \"origin\": \"五莲县院西乡范家车村\"}]}";
                NLPResponseDto nlpResponseDto = JSON.parseObject(nlpResponseStr, NLPResponseDto.class);
                List<PersonTriples> person_triples = nlpResponseDto.getPerson_triples();
                if(!person_triples.isEmpty()){
                    for (PersonTriples personTriples:person_triples) {
                        personTriples.setId(UUID.randomUUID().toString());
                        personTriples.setTaskid(jobId);
                        personTriplesMongoDao.save(personTriples);
                    }
                }
                //todo 解析NLP返回的字符串
                //                nlpResponseStr=""
                List<String> personItems = new ArrayList<>();
                List<String> worldItems = new ArrayList<>();
                List<String> orgItems = new ArrayList<>();
                List<String> eventItems = new ArrayList<>();
                for(Item item:nlpResponseDto.getItems()){
                    switch (item.getWordtag_label()){
                        case "人物类_实体":
                            personItems.add(JSON.toJSONString(item));
                            PersonTriples personTriples = new PersonTriples();
                            personTriples.setName(item.getItem());
                            personTriples.setId(UUID.randomUUID().toString());
                            personTriples.setTaskid(jobId);
                            personTriplesMongoDao.save(personTriples);
                            break;
                        case "世界地区类":
                            worldItems.add(JSON.toJSONString(item));
                            break;
                        case "组织机构类":
                            orgItems.add(JSON.toJSONString(item));
                            break;
                        case "事件":
                            eventItems.add(JSON.toJSONString(item));
                            break;
                    }
                }

                if (!personItems.isEmpty()){
                    List<String> personDistinctList = personItems.stream().distinct().collect(Collectors.toList());
                    docEsBean.setPerson(JSONArray.toJSONString(personDistinctList));

                }
                if (!worldItems.isEmpty()){
                    List<String> worldDistinctList = worldItems.stream().distinct().collect(Collectors.toList());
                    docEsBean.setGeographyInfo(JSONArray.toJSONString(worldDistinctList));
                }
                if (!orgItems.isEmpty()){
                    List<String> orgDistinctList = orgItems.stream().distinct().collect(Collectors.toList());
                    docEsBean.setOrganization(JSONArray.toJSONString(orgDistinctList));
                }
                if (!eventItems.isEmpty()){
                    List<String> eventDistinctList = eventItems.stream().distinct().collect(Collectors.toList());
                    docEsBean.setEvent(JSONArray.toJSONString(eventDistinctList));
                }

//                docEsService.save(docEsBean);
                docMongoDao.save(docEsBean);


            }

        }
        DocJob docJob = new DocJob();
        docJob.setId(jobId);
        docJob.setTaskStatus("解析完成");
        docJobService.updateById(docJob);
        return Result.getSuccessResult(contentDtos);

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
     * 构造文件名称
     * @param pageNum
     * @return
     */

    String formatecontentFileName(Integer pageNum){
        String formateStr = 100000 + pageNum + "";

        return "T"+formateStr.substring(1, formateStr.length())+"_00.txt";

//            T00014_00.txt
    }


    /**
     * 获取两个章节之间的文本
     * @param fileName
     * @return
     */

    public String readSubContentFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            System.out.println("获取两个章节之间的文本，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                //页首第一个char为空
                if(line==1){
                    char[] chars = tempString.toCharArray();
                    char[] newChars =new char[chars.length-1];
                    for (int i = 0; i < chars.length-1; i++) {
                        newChars[i]=chars[i+1];
                    }
                    tempString=String.valueOf(newChars);
                }


                String jiePatternStr = "^(\\u7b2c).+(\\u8282)";
                String zhangPatternStr = "^(\\u7b2c).+(\\u7ae0)";
                String bianPatternStr = "^(\\u7b2c).+(\\u7f16)";
//                String patternStr = "^(\\u7b2c).+(\\u7f16|\\u7ae0|\\u8282)";
                Pattern jiePattern = Pattern.compile(jiePatternStr);
                Pattern zhangPattern = Pattern.compile(zhangPatternStr);
                Pattern bianPattern = Pattern.compile(bianPatternStr);

                Matcher jieMatcher = jiePattern.matcher(tempString);
                Matcher zhangMatcher = zhangPattern.matcher(tempString);
                Matcher bianMatcher = bianPattern.matcher(tempString);
                if(matchTime == 2)
                    return stringBuffer.toString();

                if(matchTime == 1){
//                    System.out.println("line " + line + ": " + tempString);

                    if(jieMatcher.find()||zhangMatcher.find()||bianMatcher.find()){
                        matchTime++;
                        if(matchTime == 2)
                            return stringBuffer.toString();
                    }
                    //文本内行间拼接
                    stringBuffer.append(tempString+"\r\n");
                }

                if (jieMatcher.find()){
                    matchTime++;
//                    System.out.println(tempString);
                }


                // 显示行号
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

        return stringBuffer.toString();
    }

}

