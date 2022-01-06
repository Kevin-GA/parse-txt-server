package com.newtranx.cloud.edit.mongoDao;

import com.newtranx.cloud.edit.entities.DocEsBean;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocMongoDao extends MongoRepository<DocEsBean, String> {

    List<DocEsBean> findByBianzhangjie(String bianzhangjie);

    List<DocEsBean> findByDescLikeAndDocId(String desc,Integer docId);

    List<DocEsBean> findByDocId(Integer docId);
}
