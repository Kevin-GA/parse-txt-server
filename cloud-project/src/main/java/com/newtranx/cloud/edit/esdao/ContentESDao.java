package com.newtranx.cloud.edit.esdao;

import com.newtranx.cloud.edit.entities.ContentEsBean;

import com.newtranx.cloud.edit.entities.DocEsBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContentESDao extends ElasticsearchRepository<ContentEsBean, Long> {

    List<ContentEsBean> findByFileId(String fileId);

}
