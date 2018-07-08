package com.njust.repo;

import com.njust.vo.UserVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepo extends MongoRepository<UserVo, String>, UsersRepoCustom {
    UserVo findById(String id);
    UserVo insert(UserVo userVo);
    UserVo save(UserVo userVo);
    Page<UserVo> findAllByRole(String role, Pageable pageable);
    Page<UserVo> findAll(Pageable pageable);
    void delete(UserVo userVo);
}
