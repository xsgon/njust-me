package com.njust.service;

import com.njust.Exception.AlreadyExistsException;
import com.njust.repo.UsersRepo;
import com.njust.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UsersRepo usersRepo;

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
			return usersRepo.findById(userId);
	}


	public UserVo addNewUser(UserVo user) throws AlreadyExistsException {
        UserVo oldUser = this.getUserInfoByUserId(user.getId());

		if (oldUser != null) {
		    throw new AlreadyExistsException("用户ID已存在");
        }

		return usersRepo.insert(user);
	}

    public UserVo updateUser(UserVo user) {
        return usersRepo.save(user);
    }

    public void deleteUser(UserVo userVo) {
	    usersRepo.delete(userVo);
    }

    public Page<UserVo> findAllByRole(String role, PageRequest pageRequest) {
		return usersRepo.findAllByRole(role, pageRequest);
	}

    public Page<UserVo> findAll(PageRequest pageRequest) {
        return usersRepo.findAll(pageRequest);
    }

    // only a test for mongodb template
    public List<UserVo> customFind() {
	    return usersRepo.customFind();
    }

}
