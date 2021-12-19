package com.newtranx.cloud.edit.service.impl;

import com.newtranx.cloud.edit.dao.TaskDao;
import com.newtranx.cloud.edit.entities.Task;
import com.newtranx.cloud.edit.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Author: niujiaxin
 * @Date: 2021-02-05 01:10
 */
@Service("taskService")
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Override
    public int assign(Task task) {
        int res = 0;
        if(task.getTaskId()!=null)
            res = taskDao.update(task);
        else
            res= taskDao.create(task);
        return res;
    }

    @Override
    public List<String> getReduceProcess(List<Long> fileIds){
        //查询定义的工作流程
        //todo 查询sql project_process
        List<String> listMap = new ArrayList<>();
        listMap.add("翻译");
        listMap.add("编辑");
        listMap.add("校对");
        //根据文件id获取task
        List<List<String>> arrayLists = new ArrayList<>();
        for (Long fileId:fileIds) {
            arrayLists.add(taskDao.getTasksString(fileId));
        }
        //不同task的type取并集
        List<String> listAll = arrayLists.get(0).parallelStream().collect(toList());
        for (List<String> list:arrayLists) {
            List<String> listAll2 = list.parallelStream().collect(toList());
            listAll.addAll(listAll2);
        }
        //与预定义工作流程取差集获取未分配的类型
        List<String> reduce = listMap.stream().filter(item -> !listAll.contains(item)).collect(toList());

        return reduce;
    }
}
