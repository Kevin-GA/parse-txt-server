package com.newtranx.cloud.edit.service.impl;

import com.newtranx.cloud.edit.dao.ProjectFileDao;
import com.newtranx.cloud.edit.dao.TaskDao;
import com.newtranx.cloud.edit.dto.ProjectFileVo;
import com.newtranx.cloud.edit.entities.ProjectFile;
import com.newtranx.cloud.edit.entities.Task;
import com.newtranx.cloud.edit.service.ProjectFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: niujiaxin
 * @Date: 2021-02-04 23:11
 */

@Service("projectFileService")
public class ProjectFileServiceImpl implements ProjectFileService {

    @Autowired
    private ProjectFileDao projectFileDao;

    @Autowired
    private TaskDao taskDao;

    @Override
    public Object uploadFile(MultipartFile file) {
        return null;
    }

    @Override
    public List<ProjectFileVo> getFilesByProjectId(Long projectId) {
        List<ProjectFileVo> projectFileVos = new ArrayList<>();
        //获取项目关联文件
        List<ProjectFile> fileList = projectFileDao.getListByProjectId(projectId);
        for (ProjectFile projectFile:fileList){
            ProjectFileVo projectFileVo = new ProjectFileVo();
            //查询每个文件对应的任务分配的人
            List<Task> tasks = taskDao.getTasksByFileId(projectFile.getFileId());
            projectFileVo.setTaskList(tasks);
            projectFileVo.setProjectFile(projectFile);
            projectFileVos.add(projectFileVo);
        }
        return projectFileVos;
    }

    @Override
    public List<ProjectFile> getUnAssignFilesByProjectId(Long projectId) {
        return projectFileDao.getUnassignFilesByProjectId(projectId);
    }

    @Override
    public int addFile(ProjectFile projectFile) {
        return projectFileDao.create(projectFile);
    }

    @Override
    public int updateFile(ProjectFile projectFile) {
        return projectFileDao.update(projectFile);
    }

    @Override
    public int delFile(Long fileId) {
        ProjectFile projectFile = new ProjectFile();
        projectFile.setFileId(fileId);
        projectFile.setIsDel(1);
        return updateFile(projectFile);
    }

    @Override
    public List<Task> getTaskList(Long fileId) {
        return null;
    }
}
