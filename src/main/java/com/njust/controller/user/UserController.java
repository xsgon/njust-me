package com.njust.controller.user;

import com.njust.service.UserService;
import com.njust.vo.UserVo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

import com.njust.model.response.*;
import com.njust.model.user.*;

@RestController
@Api(tags = {"Authentication"})
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Gets current user information", response = OperationResponse.class)
    @RequestMapping(value = "/read/current_user", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse getUserInfo() {
        String loggedInUserId = userService.getLoggedInUserId();
        UserVo userVo = userService.getUserInfoByUserId(loggedInUserId);
        OperationResponse response = new OperationResponse();
        userVo.set_id("");
        response.setBody(userVo);

        Object u = SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        JSONObject json = new JSONObject(u);
        log.info("user is " + json);


        return response;
    }

    @ApiOperation(value = "add new user", response = OperationResponse.class)
    @RequestMapping(value = "/write/new_user", method = RequestMethod.POST, produces = {"application/json"})
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse addNewUser(@RequestBody UserVo userVo) {
        Long now = System.currentTimeMillis();
        userVo.setCreateTime(now);
        userVo.setUpdateTime(now);
        userVo.setPassword(new BCryptPasswordEncoder().encode(userVo.getPassword()));

        int num = userService.addNewUser(userVo);
        OperationResponse response = new OperationResponse();
        if (num == 0) {
            response.setCode(ErrorCode.CODE_USER_EXISTS);
            response.setMessage(ErrorCode.MSG_USER_EXISTS);
        }
        return response;
    }


    @ApiOperation(value = "update user information", response = OperationResponse.class)
    @RequestMapping(value = "/update/user", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse updateUser(@RequestBody UserVo userVo) {
        UserVo dbUser = userService.getUserInfoByUserId(userVo.getId());
        UserVo currUser = userService.getLoggedInUser();
        OperationResponse response = new OperationResponse();
        boolean auth = true;

        // check if user exists
        if (dbUser == null) {
            response.setCode(ErrorCode.CODE_USER_NOT_EXISTS);
            response.setMessage(ErrorCode.MSG_USER_NOT_EXISTS);
            return response;
        }

        // check auth
        if (currUser.getRole().equals(Role.ROLE_NORMAL.name())
                && !currUser.getId().equals(userVo.getId())) {
            // normal user can only modify himself information
            auth = false;
        } else if (!currUser.getId().equals(dbUser.getId())
                && currUser.getRole().equals(Role.ROLE_ADMIN.name())
                && dbUser.getRole().equals(Role.ROLE_ADMIN.name())) {
            // admin user can not modify other admin's information
            auth = false;
        } else if (currUser.getRole().equals(Role.ROLE_SUPER_ADMIN.name())) {
            auth = false;
        }

        if (!auth) {
            response.setCode(ErrorCode.CODE_ACCESS_DENY);
            response.setMessage(ErrorCode.MSG_ACCESS_DENY);
        } else {
            int num = 0;
            userVo.setRole(dbUser.getRole());
            userVo.setPassword(dbUser.getPassword());
            userVo.setActive(dbUser.getActive());
            userVo.set_id(dbUser.get_id());
            userVo.setUpdateTime(System.currentTimeMillis());

            num = userService.updateUser(userVo);
            if (num != 1) {
                response.setCode(ErrorCode.CODE_UPDATE_USER_FAILED);
                response.setMessage(ErrorCode.MSG_UPDATE_USER_FAILED);
            }
        }

        return response;
    }

    @ApiOperation(value = "delete user", response = OperationResponse.class)
    @RequestMapping(value = "/delete/user", method = RequestMethod.POST, produces = {"application/json"})
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse deleteUser(@RequestBody UserVo userVo) {
        UserVo currUser = userService.getLoggedInUser();
        UserVo dbUser = userService.getUserInfoByUserId(userVo.getId());
        OperationResponse response = new OperationResponse();
        boolean auth = true;

        if (dbUser == null) {
            response.setCode(ErrorCode.CODE_USER_NOT_EXISTS);
            response.setMessage(ErrorCode.MSG_USER_NOT_EXISTS);
            return response;
        }

        // auth check
        if (userVo.getId().equals(currUser.getId())) {
            // one can not delete himself
            auth = false;
        } else if (currUser.getRole().equals(Role.ROLE_ADMIN.name())
                && dbUser.getRole().equals(Role.ROLE_ADMIN.name())) {
            // admin can not delete other admin
            auth = false;
        } else if (dbUser.getRole().equals(Role.ROLE_SUPER_ADMIN.name())) {
            // you can not delete super admin here
            auth = false;
        }

        if (!auth) {
            response.setCode(ErrorCode.CODE_ACCESS_DENY);
            response.setMessage(ErrorCode.MSG_ACCESS_DENY);
        } else {
            userVo.set_id(dbUser.get_id());
            userService.deleteUser(userVo);
        }

        return response;
    }

}
