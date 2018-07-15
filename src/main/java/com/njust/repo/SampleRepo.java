package com.njust.repo;

import com.njust.vo.SampleVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SampleRepo extends MongoRepository<SampleVo, String> {
    SampleVo findBy_id(String _id);
    Page<SampleVo> findAllByCreateTimeBetween(long startTime, long endTime, Pageable pageable);
}
