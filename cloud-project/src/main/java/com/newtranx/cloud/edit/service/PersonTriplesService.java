package com.newtranx.cloud.edit.service;

import com.newtranx.cloud.edit.dto.PersonTriples;

import java.util.List;

public interface PersonTriplesService {

    void save (PersonTriples personTriples);

    List<PersonTriples> findByTaskid(Integer taskid);
}
