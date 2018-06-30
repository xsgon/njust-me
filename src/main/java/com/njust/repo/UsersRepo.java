package com.njust.repo;

import com.njust.vo.UserVo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepo extends MongoRepository<UserVo, String> {
    public UserVo findById(String id);
}
