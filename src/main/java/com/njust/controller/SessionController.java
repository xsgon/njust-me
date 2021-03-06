package com.njust.controller;

import com.njust.model.response.ErrorCode;
import com.njust.model.response.OperationResponse;
import com.njust.po.PageParam;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(tags = {"Authentication"})
@Slf4j
public class SessionController {

    @Autowired
    private UsersRepo usersRepo;

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Will return a security token, which must be passed in every request", response = OperationResponse.class)})
    @RequestMapping(value = "/session", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResponse newSession(@RequestBody PageParam param, HttpServletRequest request, HttpServletResponse response) {
        log.error("received /session request");  // 仅测试用
        return new OperationResponse();
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Will do log out", response = OperationResponse.class)})
    @RequestMapping(value = "/logout", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OperationResponse sessionLogout() {
        log.info("received /logout request");

        OperationResponse resp = new OperationResponse();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        resp.setCode(ErrorCode.CODE_SUCCESS);
        resp.setMessage("logout Success");

        return resp;
    }

}
