package com.njust.controller;

import com.njust.model.response.ErrorCode;
import com.njust.model.response.OperationResponse;
import com.njust.repo.UsersRepo;
import com.njust.vo.UserVo;
import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.*;

import com.njust.model.session.*;
import com.njust.model.user.*;

/*
This is a dummy rest controller, for the purpose of documentation (/session) path is map to a filter
 - This will only be invoked if security is disabled
 - If Security is enabled then SessionFilter.java is invoked
 - Enabling and Disabling Security is done at config/applicaton.properties 'security.ignored=/**'
*/

@RestController
@Api(tags = {"Authentication"})
@Slf4j
public class SessionController {

    @Autowired
    private UsersRepo usersRepo;

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Will return a security token, which must be passed in every request", response = OperationResponse.class)})
    @RequestMapping(value = "/session", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResponse newSession(@RequestBody Login login, HttpServletRequest request, HttpServletResponse response) {
//        UserMapper user = userRepo.findOneByUserIdAndPassword(login.getUsername(), login.getPassword()).orElse(null);
        log.error("received /session request");
//        // 从数据库里得到
//        UserVo user = usersRepo.findById(login.getUsername());
//
//        SessionResponse resp = new SessionResponse();
//        SessionItem sessionItem = new SessionItem();
//        if (user != null) {
//            sessionItem.setToken("xxx.xxx.xxx");
//            sessionItem.setUserId(user.getId());
//            sessionItem.setEmail(user.getEmail());
//            sessionItem.setRole(user.getRole());
//
//            resp.setCode(ErrorCode.CODE_SUCCESS);
//            resp.setMessage("Dummy Login Success");
//            resp.setItem(sessionItem);
//        } else {
//            resp.setCode(ErrorCode.CODE_AUTH_ERROR);
//            resp.setMessage(ErrorCode.MSG_AUTH_ERROR);
//        }
        return new OperationResponse();
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Will do log out", response = OperationResponse.class)})
    @RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResponse sessionLogout(@RequestBody Login login, HttpServletRequest request, HttpServletResponse response) {
//        UserMapper user = userRepo.findOneByUserIdAndPassword(login.getUsername(), login.getPassword()).orElse(null);
        log.error("received /logout request");

        OperationResponse resp = new OperationResponse();
        SessionItem sessionItem = new SessionItem();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        sessionItem.setUserId("nobody");
        if (auth != null) {
            sessionItem.setUserId(auth.getName());
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        resp.setCode(ErrorCode.CODE_SUCCESS);
        resp.setMessage("logout Success");
        resp.setBody(sessionItem);

        return resp;
    }

}
