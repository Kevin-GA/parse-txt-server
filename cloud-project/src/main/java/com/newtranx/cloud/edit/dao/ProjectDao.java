package com.newtranx.cloud.edit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtranx.cloud.edit.dto.ProjectVo;
import com.newtranx.cloud.edit.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.newtranx.cloud.edit.entities.Project;
import java.util.List;

@Mapper
public interface ProjectDao extends BaseMapper<Project>
{
    public int create(@Param("entity")Project project);

    public Project getProjectById(@Param("projectId") Long projectId);

    public List<Project> getList( Project project);

    public Integer isdelByProjectId(@Param("projectId") Long projectId);

    public List<String> getUsers(@Param("projectId") Long projectId);

    int update(@Param("entity")Project project);
}
