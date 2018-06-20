package com.njust.repo;

import com.njust.vo.MainTestVo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MainTestRepo extends MongoRepository<MainTestVo, String> {

}
