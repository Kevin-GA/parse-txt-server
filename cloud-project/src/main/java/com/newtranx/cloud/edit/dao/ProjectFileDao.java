package com.newtranx.cloud.edit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtranx.cloud.edit.entities.Project;
import com.newtranx.cloud.edit.entities.ProjectFile;
import com.newtranx.cloud.edit.entities.ProjectProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectFileDao extends BaseMapper<ProjectProcess>
{
    int create(@Param("entity") ProjectFile projectFile);

    int update(@Param("entity") ProjectFile projectFile);

    List<ProjectFile> getListByProjectId(@Param("projectId") Long projectId);

    List<ProjectFile> getUnassignFilesByProjectId(@Param("projectId") Long projectId);
}
