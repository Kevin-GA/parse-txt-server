package com.newtranx.cloud.edit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newtranx.cloud.edit.dto.ProjectParamVo;
import com.newtranx.cloud.edit.entities.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther Charlie
 * @create 2021-02-01 05:40
 */
public interface ProjectService
{
    public int create(Project project);

    public int del(@Param("projectId") Long projectId);

    public Project getProjectById(@Param("projectId") Long projectId);

    public List<Project> getList(Project project);

    public IPage<Project> getPage(ProjectParamVo projectParam, Page<Project> page);

    int update(Project project);

}
