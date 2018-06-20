package com.njust.controller.user;

import com.njust.mapper.UserMapper;
import com.njust.vo.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

	public String getLoggedInUserId(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth==null){
            return "nosession";
        }
		return auth.getName();
	}


	public UserVo getLoggedInUser() {
		String loggedInUserId = this.getLoggedInUserId();
		return this.getUserInfoByUserId(loggedInUserId);
	}

	public UserVo getUserInfoByUserId(String userId){
			return userMapper.getUserById(userId);
	}


	public boolean addNewUser(UserVo user) {
		UserVo oldUser = this.getUserInfoByUserId(user.getId());
		if (oldUser == null){
			return userMapper.insertUser(user) == 1;
		}

		return true;
	}

}
