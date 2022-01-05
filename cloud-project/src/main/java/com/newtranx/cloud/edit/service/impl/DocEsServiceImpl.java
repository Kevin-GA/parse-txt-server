package com.newtranx.cloud.edit.service.impl;

import com.newtranx.cloud.edit.entities.DocEsBean;
import com.newtranx.cloud.edit.esdao.DocESDao;
import com.newtranx.cloud.edit.service.DocEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("DocEsService")
public class DocEsServiceImpl implements DocEsService {
    @Resource
    private DocESDao docESDao;

    @Override
    public Iterable<DocEsBean> findAll() {
        return docESDao.findAll();
    }

    @Override
    public void save(List<DocEsBean> list) {
        docESDao.saveAll(list);
    }

    @Override
    public void save(DocEsBean bean) {
        docESDao.save(bean);
    }

    @Override
    public List<DocEsBean> findByBianzhangjie(String bianzhangjie) {
        return docESDao.findByBianzhangjie(bianzhangjie);
    }

    @Override
    public List<DocEsBean> findByNameOrDesc(String keyWord, String desc) {
        return docESDao.findByNameOrDesc(keyWord,desc);
    }

    @Override
    public List<DocEsBean> findByDesc(String desc) {
        return docESDao.findByDesc(desc);
    }
}
