package com.newtranx.cloud.edit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtranx.cloud.edit.common.enums.FailureCodeEnum;
import com.newtranx.cloud.edit.dto.*;
import com.newtranx.cloud.edit.entities.Project;
import com.newtranx.cloud.edit.entities.ProjectFile;
import com.newtranx.cloud.edit.entities.Task;
import com.newtranx.cloud.edit.entities.User;
import com.newtranx.cloud.edit.service.ProjectFileService;
import com.newtranx.cloud.edit.service.ProjectService;
import com.newtranx.cloud.edit.service.TaskService;
import com.newtranx.cloud.edit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.newtranx.cloud.edit.common.entities.CommonResult;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: niujiaxin
 * @Date: 2021-02-01 23:49
 */
@RestController
@Slf4j
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectFileService projectFileService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;


    //新增项目
    @PostMapping("project/create")
    public CommonResult<Object> create(Project project){
        int res = projectService.create(project);
        if(res>0)
            return new CommonResult<Object>(200,"success",project);
        else
            return new CommonResult<Object>(500,"fail",project);
    }

    //删除项目
    @GetMapping("project/del")
    public CommonResult<String> del(Long projectId){
        int res = projectService.del(projectId);
        if(res>0)
            return new CommonResult<String>(200,"success",projectId.toString());
        else
            return new CommonResult<String>(500,"fail",projectId.toString());

    }

    //项目分页查询
    @PostMapping("/project/getPage")
    public CommonResult<IPage> page(@RequestBody ProjectParamVo projectParamVo, Integer pageNum, Integer pageSize){
        Page<Project> page = new Page<Project>(pageNum,pageSize);
        IPage<Project> pageModel = projectService.getPage(projectParamVo,page);
        return new CommonResult<IPage>(200,"success",pageModel);

    }

    //文件上传
    @PostMapping("/project/uploadfile")
    public CommonResult<ProjectFile> uploadfile(MultipartFile file,String userId, String projectId){
        ProjectFile projectFile = new ProjectFile();
        projectFile.setUserId(userId);
        projectFile.setProjectId(projectId);
        String[] splitFileName = file.getOriginalFilename().split("\\.");
        projectFile.setFileName(splitFileName[0]);
        projectFile.setFilePath("test/path");
        projectFile.setFileExt(splitFileName[1]);
        projectFile.setFileSize(Integer.parseInt(file.getSize()+""));
        //调用文件上传接口
//        projectFileService.uploadFile(file);
        int res = projectFileService.addFile(projectFile);
        if(res>0)
            return new CommonResult<ProjectFile>(200,"success",projectFile);
        else
            return new CommonResult<ProjectFile>(500,"fail",projectFile);

    }

    //获取项目详情
    @GetMapping("/project/detail")
    public CommonResult<ProjectVo> detail(Long projectId){
        ProjectVo projectVo = new ProjectVo();
        //项目详情
        projectVo.setProject(projectService.getProjectById(projectId));
        //文件列表
        projectVo.setProjectFiles(projectFileService.getFilesByProjectId(projectId));
        return new CommonResult<>(200,"success",projectVo);

    }

    //修改项目
    @PostMapping("project/update")
    public CommonResult<Project> update(Project project){
        int res = projectService.update(project);
        if(res>0)
            return new CommonResult<Project>(200,"success",project);
        else
            return new CommonResult<Project>(500,"fail",project);
    }

    //文件批量上传
    @PostMapping("/project/batchuploadfile")
    public CommonResult<List<ProjectFile>> uploadfiles(MultipartFile[] file, Long fileId,String userId, String projectId){
        List<ProjectFile> projectFiles = new ArrayList<>();
        for (MultipartFile multipartFile : file) {
            projectFiles.add(uploadfile(multipartFile,userId,projectId).getData());
        }
        if(fileId!=null)
            projectFileService.delFile(fileId);
        return new CommonResult<>(200,"success",projectFiles);

    }

    //删除文件
    @GetMapping("/project/delfile")
    public CommonResult<Object> delfile(Long fileId){
        try {
            projectFileService.delFile(fileId);
            return new CommonResult<>(200,"success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommonResult<>(500,"failed");
    }

    //获取项目相关人员
    @GetMapping("/project/getUsers")
    public CommonResult<Object> uploadfiles(Long projectId){
        List<UserVo> userVos = new ArrayList<>();
        //通过项目id查询可分配人员
        List<User> usersByProjectId = userService.getUsersByProjectId(projectId);
        for (User user:usersByProjectId) {
            UserVo userVo = new UserVo();
            userVo.setUserId(user.getUserId());
            userVo.setUsername(user.getUsername());
            userVos.add(userVo);
        }

        return new CommonResult<>(200,"success",userVos);

    }

    //分配时候查询没有百分百分配的文档
    @GetMapping("/project/getUnAssignFiles")
    public CommonResult<Object> getProcess(Long projectId) {
        List<ProjectFile> list = null;
        try{
            list = projectFileService.getUnAssignFilesByProjectId(projectId);
            return new CommonResult<>(200,"success",list);

        }catch (Exception e){
            return new CommonResult<>(500,"failed");

        }

    }

    //获取待分配的工作流程
    @GetMapping("/project/getProcess")
    public CommonResult<Object> getProcess(@RequestParam("fileIds") List<Long> fileIds){
        List<String> reduceProcess = taskService.getReduceProcess(fileIds);
        return new CommonResult<Object>(200,"success",reduceProcess);
    }

    //分配
    @PostMapping("/project/assgin")
    public CommonResult<Object> assgin(Task task){
        int res = taskService.assign(task);
        if(res>0)
            return new CommonResult<Object>(200,"success");
        else
            return new CommonResult<Object>(500,"fail");
    }



}
