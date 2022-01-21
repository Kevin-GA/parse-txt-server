package com.newtranx.cloud.edit.esdao;

import com.newtranx.cloud.edit.entities.DemoEsBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DemoESDao extends ElasticsearchRepository<DemoEsBean, Long> {

    List<DemoEsBean> findByName(String name);

}
