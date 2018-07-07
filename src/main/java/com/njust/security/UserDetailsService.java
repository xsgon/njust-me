package com.njust.security;

import com.njust.repo.UsersRepo;
import com.njust.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UsersRepo usersRepo;

    private boolean hideUserNotFoundExceptions = false;


    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public final TokenUser loadUserByUsername(String username) throws UsernameNotFoundException, DisabledException {
        log.info("auth user " + username + " with database info");

        // 从数据库里取出用户信息
        UserVo user = usersRepo.findById(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

//        log.info("user info " + user.toString());

//        //test only
//        User user = new User();
//        user.setUserId("user");
//        user.setActive(true);
//        user.setRole(ADMIN);
//        user.setPassword("$2a$10$Mf0kwi6u.AAMnBjYK.BrKuJ4yU82H4xO48Bk3Iqq4.uYd2Ph82VQu");

        TokenUser currentUser;
        if (user.getActive() == 1){
            currentUser = new TokenUser(user);
        } else {
            throw new DisabledException("UserMapper is not activated (Disabled UserMapper)");
            //If pending activation return a disabled user
            //currentUser = new TokenUser(user, false);
        }

        detailsChecker.check(currentUser);
        return currentUser;
    }
}
