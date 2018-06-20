package com.njust.security;

import com.njust.model.user.Role;
import com.njust.vo.UserVo;
import org.springframework.security.core.authority.AuthorityUtils;

public class TokenUser extends org.springframework.security.core.userdetails.User {
    private UserVo user;

    //For returning a normal user
    public TokenUser(UserVo user) {
        super( user.getId(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()  )  );
        //super(user.getUserName(), user.getPassword(), true, true, true, true,  AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public UserVo getUser() {
        return user;
    }

    public Role getRole() {
        return user.getRole();
    }
}
