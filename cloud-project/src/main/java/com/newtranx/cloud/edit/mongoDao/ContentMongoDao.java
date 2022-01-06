package com.newtranx.cloud.edit.mongoDao;

import com.newtranx.cloud.edit.entities.ContentEsBean;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContentMongoDao extends MongoRepository<ContentEsBean, String> {

    List<ContentEsBean> findByFileId(String fileId);
}
