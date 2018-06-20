package com.njust.mapper;

import com.njust.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    UserVo getUserById(@Param("id") String id);

    int updateUserProfile(UserVo user);
    int updateUserPassword(UserVo user);

    int insertUser(UserVo user);
}
