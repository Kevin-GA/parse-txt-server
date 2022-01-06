package com.newtranx.cloud.edit.mongoDao;

import com.newtranx.cloud.edit.dto.PersonTriples;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PersonTriplesMongoDao extends MongoRepository<PersonTriples, String> {

    List<PersonTriples> findByTaskid(Integer taskid);
}
