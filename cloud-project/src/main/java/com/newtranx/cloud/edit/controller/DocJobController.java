package com.newtranx.cloud.edit.controller;


import com.newtranx.cloud.edit.common.entities.Result;
import com.newtranx.cloud.edit.entities.DocJob;
import com.newtranx.cloud.edit.service.DocJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
@Api(tags = "任务文档接口")
public class DocJobController {

    private static final Logger LOG = LoggerFactory.getLogger(DocJobController.class);

    @Resource
    private DocJobService docJobService;

    /**
     * 创建任务并上传文件
     * @param docJob
     * @param file
     * @return
     */
    @PostMapping("/create")
    @ApiOperation(value = "创建作业，并上传文件")
    public Result<Object> create(DocJob docJob,@RequestParam("file") MultipartFile file){
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



}

