package com.newtranx.cloud.edit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newtranx.cloud.edit.entities.ProjectFile;
import com.newtranx.cloud.edit.entities.ProjectProcess;
import com.newtranx.cloud.edit.entities.Task;
import com.newtranx.cloud.edit.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskDao extends BaseMapper<ProjectProcess>
{

    int create(@Param("entity") Task task);

    int update(@Param("entity") Task task);

    List<Task> getTasksByFileId(@Param("fileId") Long fileId);

    List<Task> getTasksByFileIds(@Param("list") List<Long> fileIds);

    List<String> getTasksString(@Param("fileId") Long fileId);


}
