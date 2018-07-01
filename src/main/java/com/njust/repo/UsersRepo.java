package com.njust.repo;

import com.njust.vo.UserVo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepo extends MongoRepository<UserVo, String> {
    UserVo findById(String id);
    UserVo insert(UserVo userVo);
    UserVo save(UserVo userVo);
}
