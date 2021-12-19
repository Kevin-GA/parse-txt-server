package com.newtranx.cloud.edit.service;

import com.newtranx.cloud.edit.entities.User;

import java.util.List;

/**
 * @Author: niujiaxin
 * @Date: 2021-02-05 01:09
 */
public interface UserService {

    List<User> getUsersByProjectId(Long projectId);
}
