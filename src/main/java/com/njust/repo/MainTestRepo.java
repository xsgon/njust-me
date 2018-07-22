package com.njust.repo;

import com.njust.vo.MainTestVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MainTestRepo extends MongoRepository<MainTestVo, String> {
    MainTestVo findBy_id(String id);
    Page<MainTestVo> findAllByTestStartBetween(long startTime, long endTime, Pageable pageable);
    int countAllByTestStartBetween(long startTime, long endTime);
    MainTestVo findByTestId(String testId);
}
