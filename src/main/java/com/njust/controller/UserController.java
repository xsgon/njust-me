package com.njust.controller;

import com.njust.Exception.AlreadyExistsException;
import com.njust.po.PageParam;
import com.njust.service.UserService;
import com.njust.vo.UserVo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;

import com.njust.model.response.*;
import com.njust.model.user.*;

import java.util.ArrayList;
import java.util.List;

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
        UserVo userVo = userService.getLoggedInUser();
        OperationResponse response = new OperationResponse();
        userVo.hideSensitiveInfo();
        response.setBody(userVo);

        return response;
    }

    @ApiOperation(value = "read all users under management", response = OperationResponse.class)
    @RequestMapping(value = "/read/all", method = RequestMethod.POST, produces = {"application/json"})
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse getUsers(@RequestBody PageParam params) {
        UserVo userVo = userService.getLoggedInUser();
        params.formalize();

        PageRequest pgReq = params.buildPageRequest("createTime");;
        Page<UserVo> pgUsers;
        if (userVo.getRole().equals(Role.ROLE_SUPER_ADMIN.name())) {
            pgUsers = userService.findAll(pgReq);
        } else {
            pgUsers = userService.findAllByRole(Role.ROLE_NORMAL.name(), pgReq);
        }

        for (UserVo u : pgUsers.getContent()) {
            u.hideSensitiveInfo();
        }

        OperationResponse response = new OperationResponse();
        response.setPageAndBody(pgUsers);

        return response;
    }

    @ApiOperation(value = "test", response = OperationResponse.class)
    @RequestMapping(value = "/read/special", method = RequestMethod.POST, produces = {"application/json"})
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse readTest() {
        OperationResponse response = new OperationResponse();
        response.setBody(userService.customFind());

        return response;
    }

    @ApiOperation(value = "add new user", response = OperationResponse.class)
    @RequestMapping(value = "/add/new_user", method = RequestMethod.POST, produces = {"application/json"})
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse addNewUser(@RequestBody UserVo userVo) {
        boolean auth = true;
        UserVo currUser = userService.getLoggedInUser();
        OperationResponse response = new OperationResponse();

        // auth check
        if (currUser.getRole().equals(userVo.getRole())) {
            // can not create new user of same level
            auth = false;
        } else if (userVo.getRole().equals(Role.ROLE_SUPER_ADMIN.name())) {
            // can not create super admin
            auth = false;
        }

        if (!auth) {
            response.setCode(ErrorCode.CODE_ACCESS_DENY);
            response.setMessage(ErrorCode.MSG_ACCESS_DENY);
        } else {
            Long now = System.currentTimeMillis();
            userVo.setCreateTime(now);
            userVo.setUpdateTime(now);
            userVo.setActive(1);
            userVo.setPassword(new BCryptPasswordEncoder().encode(userVo.getPassword()));

            UserVo u = null;
            try {
                u = userService.addNewUser(userVo);
            } catch (AlreadyExistsException e) {
                response.setCode(ErrorCode.CODE_USER_EXISTS);
                response.setMessage(ErrorCode.MSG_USER_EXISTS);
            }

            if (u == null) {
                response.setCode(ErrorCode.CODE_SYSTEM_ERROR);
                response.setMessage(ErrorCode.MSG_SYSTEM_ERROR);
            }
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

            UserVo u = userService.updateUser(userVo);
            if (u == null) {
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

    @ApiOperation(value = "update self profile", response = OperationResponse.class)
    @RequestMapping(value = "/update/self", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse updateSelf(@RequestBody UserVo userVo) {
        OperationResponse response = new OperationResponse();
        UserVo currUser = userService.getLoggedInUser();
        currUser.setPhone(userVo.getPhone());
        currUser.setName(userVo.getName());

        UserVo dbUser = userService.updateUser(currUser);
        if (dbUser == null) {
            log.error("update user " + currUser.getId() + " failed");
            response.setCode(ErrorCode.CODE_SYSTEM_ERROR);
            response.setMessage(ErrorCode.MSG_SYSTEM_ERROR);
        }

        return response;
    }

}
