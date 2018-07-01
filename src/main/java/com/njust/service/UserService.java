package com.njust.service;

import com.njust.repo.UsersRepo;
import com.njust.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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


	public int addNewUser(UserVo user) {
        UserVo oldUser = this.getUserInfoByUserId(user.getId());
        log.info("old user is " + oldUser);
		if (oldUser == null){
		    UserVo dbUser = usersRepo.insert(user);
		    log.info("new user is " + dbUser);
		    return 1;
		}

		return 0;
	}

    public int updateUser(UserVo user) {
        UserVo dbUser = usersRepo.save(user);
        log.info("now user is " + dbUser);
        return 1;
    }

    public void deleteUser(UserVo userVo) {
	    usersRepo.delete(userVo);
    }

}
