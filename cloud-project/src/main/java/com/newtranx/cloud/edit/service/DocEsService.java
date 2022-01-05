package com.newtranx.cloud.edit.service;

import com.newtranx.cloud.edit.entities.DocEsBean;

import java.util.List;

public interface DocEsService {

    Iterable<DocEsBean> findAll();

    void save(List<DocEsBean> list);

    void save(DocEsBean bean);

    List<DocEsBean> findByBianzhangjie(String bianzhangjie);

    List<DocEsBean> findByNameOrDesc(String name,String desc);

    List<DocEsBean> findByDesc(String desc);

}
