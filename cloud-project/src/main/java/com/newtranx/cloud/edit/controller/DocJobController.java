package com.newtranx.cloud.edit.controller;


import com.newtranx.cloud.edit.common.entities.Result;
import com.newtranx.cloud.edit.entities.DocJob;
import com.newtranx.cloud.edit.service.DocJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author niujiaxin
 * @since 2021-12-27
 */
@Controller
@RequestMapping("/docJob")
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
    public Result create(DocJob docJob,@RequestParam("file") MultipartFile file){
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



}

