package com.njust.controller.user;

import com.njust.vo.UserVo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;

import com.njust.model.response.*;
import com.njust.model.user.*;

@RestController
@Api(tags = {"Authentication"})
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Gets current user information", response = UserResponse.class)
    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = {"application/json"})
    public UserResponse getUserInformation(@RequestParam(value = "name", required = false) String userIdParam, HttpServletRequest req) {

        String loggedInUserId = userService.getLoggedInUserId();

        UserVo user;
        boolean provideUserDetails = false;

        if (Strings.isNullOrEmpty(userIdParam)) {
            provideUserDetails = true;
            user = userService.getLoggedInUser();
        } else if (loggedInUserId.equals(userIdParam)) {
            provideUserDetails = true;
            user = userService.getLoggedInUser();
        } else {
            //Check if the current user is superuser then provide the details of requested user
            provideUserDetails = true;
            user = userService.getUserInfoByUserId(userIdParam);
        }

        UserResponse resp = new UserResponse();
        if (provideUserDetails) {
            resp.setCode(ErrorCode.CODE_SUCCESS);
        } else {
            resp.setCode(ErrorCode.CODE_ACCESS_DENY);
            resp.setMessage(ErrorCode.MSG_ACCESS_DENY);
        }
        resp.setData(user);
        return resp;
    }


    @ApiOperation(value = "Add new user", response = OperationResponse.class)
    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse addNewUser(@RequestBody UserVo user, HttpServletRequest req) {
        boolean userAddSuccess = userService.addNewUser(user);
        OperationResponse resp = new OperationResponse();
        if (userAddSuccess) {
            resp.setCode(ErrorCode.CODE_SUCCESS);
            resp.setMessage("User Added");
        } else {
            resp.setCode(ErrorCode.CODE_ADD_USER_FAILED);
            resp.setMessage("Unable to add user");
        }
        return resp;
    }


}
