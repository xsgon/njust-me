package com.njust.repo;

import com.njust.model.user.Role;
import com.njust.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

// PAY ATTENTION TO THE CLASS NAME !!!!!!!
// UsersRepoImpl         - correct
// UsersRepoCustomImpl   - wrong !!!!!!!!
public class UsersRepoImpl implements UsersRepoCustom {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<UserVo> customFind() {
        // here to implement the extended method
        Query query = new Query(Criteria.where("role").is(Role.ROLE_ADMIN.name()).and("name").is("admin"));
        List<UserVo> users = mongoTemplate.find(query, UserVo.class);
        return users;
    }
}
