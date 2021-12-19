package com.newtranx.cloud.edit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtranx.cloud.edit.common.enums.ProjectProcessTypeEnum;
import com.newtranx.cloud.edit.dao.ProjectDao;
import com.newtranx.cloud.edit.dao.ProjectProcessDao;
import com.newtranx.cloud.edit.dto.ProjectParamVo;

import com.newtranx.cloud.edit.entities.Project;
import com.newtranx.cloud.edit.entities.ProjectProcess;
import com.newtranx.cloud.edit.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: niujiaxin
 * @Date: 2021-02-02 00:03
 */
@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ProjectProcessDao projectProcessDao;

    @Transactional
    @Override
    public int create(Project project) {

        try {
            projectDao.create(project);
            batchInsertProjectProcess(project);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int del(Long projectId) {
        return projectDao.isdelByProjectId(projectId);
    }

    @Override
    public Project getProjectById(Long projectId) {
        Project project= projectDao.getProjectById(projectId);
        if(project!=null){
            List<ProjectProcess> listByProjectId = projectProcessDao.getListByProjectId(projectId);
            project.setProjectProgress(listByProjectId);
            project.setUsers(projectDao.getUsers(projectId));
            String processStr = "";
            for (ProjectProcess p: listByProjectId) {
                processStr += ","+p.getType().getCode();
            }
            if(processStr.length()>0) {
                processStr = processStr.substring(1, processStr.length());
            }
            project.setProcess(processStr);
            return project;
        }

        return null;
    }

    @Override
    public List<Project> getList(Project project) {
        List<Project> list=projectDao.getList(project);
        return null;
    }

    @Override
    public IPage<Project> getPage(ProjectParamVo projectParam, Page<Project> page) {
        //创建查询条件
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");//正序，降序
        queryWrapper.eq("is_del",0);
        IPage<Project> iPage = projectDao.selectPage(page,queryWrapper);
        List<Project> projectList = iPage.getRecords();
        //查询项目相关进度和人员
        for (Project project: projectList) {
            List<ProjectProcess> list = projectProcessDao.getListByProjectId(project.getProjectId());
            project.setProjectProgress(list);
            List<String> userList = projectDao.getUsers(project.getProjectId());
            project.setUsers(userList);
        }
        return iPage;
    }

    @Transactional
    @Override
    public int update(Project project) {
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("project_id", project.getProjectId());
        projectProcessDao.deleteByMap(columnMap);
        batchInsertProjectProcess(project);

        return projectDao.update(project);
    }

    //提取共用方法，批量插入ProjectProcess
    private void batchInsertProjectProcess(Project project){
        String processStr = project.getProcess();
        if(processStr!=null){
            String[] sp = processStr.split(",");
            for (String p:sp) {
                ProjectProcess projectProcess = new ProjectProcess();
                projectProcess.setProjectId(project.getProjectId());
                projectProcess.setType(ProjectProcessTypeEnum.getByCode(Integer.parseInt(p)));
                projectProcess.setCreateTime(project.getCreateTime());
                projectProcessDao.insert(projectProcess);
            }
        }

    }
}
