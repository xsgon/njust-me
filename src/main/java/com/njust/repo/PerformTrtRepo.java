package com.njust.repo;

import com.njust.vo.PerformTrtVo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PerformTrtRepo extends MongoRepository<PerformTrtVo, String> {
    PerformTrtVo findBy_id(String _id);
}
