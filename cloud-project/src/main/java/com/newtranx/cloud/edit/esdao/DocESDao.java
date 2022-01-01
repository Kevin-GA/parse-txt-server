package com.newtranx.cloud.edit.esdao;

import com.newtranx.cloud.edit.entities.DocEsBean;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DocESDao extends CrudRepository<DocEsBean, Long> {

    List<DocEsBean> findByName(String name);

    List<DocEsBean> findByNameOrDesc(String keyWord,String text);
}
