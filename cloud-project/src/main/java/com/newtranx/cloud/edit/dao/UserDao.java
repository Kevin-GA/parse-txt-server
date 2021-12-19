package com.newtranx.cloud.edit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtranx.cloud.edit.entities.ProjectFile;
import com.newtranx.cloud.edit.entities.ProjectProcess;
import com.newtranx.cloud.edit.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao extends BaseMapper<ProjectProcess>
{

    List<User> getUserByProjectId(@Param("projectId") Long projectId);
}
