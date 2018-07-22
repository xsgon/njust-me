package com.njust.controller;

import com.njust.Exception.AlreadyExistsException;
import com.njust.Exception.InvalidNamePatternException;
import com.njust.Exception.NotFoundException;
import com.njust.Utils.JsonHelper;
import com.njust.model.response.ErrorCode;
import com.njust.model.response.OperationResponse;
import com.njust.po.PageParam;
import com.njust.po.TimeRange;
import com.njust.service.MainTestService;
import com.njust.vo.MainTestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.Map;

@Api(tags = {"Common"})
@RestController
@RequestMapping(value = "/main_test", produces = {"application/json"})
@Slf4j
public class MainTestController {
    @Autowired
    MainTestService mainTestService;

    @ApiOperation(value = "get all main test", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Returns all test info.", response = OperationResponse.class)})
    @RequestMapping(value = "/read/all", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse getAll(@RequestBody PageParam params) {
        params.formalize();
        OperationResponse response = new OperationResponse();
        PageRequest pgReq = params.buildPageRequest("createTime");
        Page<MainTestVo> pgTests = mainTestService.findAll(pgReq);

        response.setPageAndBody(pgTests);

        return response;
    }

    @ApiOperation(value = "get all test in time range", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Returns all test in time range.", response = OperationResponse.class)})
    @RequestMapping(value = "/read/all_by_test_time", method = RequestMethod.POST)
    public OperationResponse getAllByTestStartRange(@RequestBody PageParam params) {
        OperationResponse response = new OperationResponse();

        params.formalize();
        PageRequest pgReq = params.buildPageRequest("testStart");
        TimeRange tr = null;

        try {
            tr = (TimeRange) params.getBody();
        } catch (Exception e) {
            log.error("cast " + JsonHelper.obj2Json(params.getBody()) + " to TimeRange failed");
        }

        if (tr != null) {
            Page<MainTestVo> testVos = mainTestService.findAllByTestStartRange(tr, pgReq);
            response.setPageAndBody(testVos);
        } else {
            response.setCode(ErrorCode.CODE_INPUT_PARAMS_ERROR);
            response.setMessage(ErrorCode.MSG_INPUT_PARAMS_ERROR);
        }

        return response;
    }

    @ApiOperation(value = "add a new test", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add a new test.", response = OperationResponse.class)})
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    @RequestMapping(value = "/add/test", method = RequestMethod.POST)
    public OperationResponse addNew(@RequestBody MainTestVo MainTestVo) {
        OperationResponse response = new OperationResponse();
        try {
            MainTestVo.setCreateTime(System.currentTimeMillis());
            MainTestVo.setUpdateTime(System.currentTimeMillis());
            MainTestVo sv = mainTestService.addNewTest(MainTestVo);
            response.setBody(sv);
        } catch (AlreadyExistsException e) {
            response.setCode(ErrorCode.CODE_RECORD_EXISTS);
            response.setMessage(ErrorCode.MSG_RECORD_EXISTS + "\n" + e.getMessage());
        } catch (InvalidNamePatternException e) {
            response.setCode(ErrorCode.CODE_INVALID_NAME_PATTERN);
            response.setMessage(ErrorCode.MSG_INVALID_NAME_PATTERN);
        }

        return response;
    }

    @ApiOperation(value = "delete a test", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "delete a test.", response = OperationResponse.class)})
    @RequestMapping(value = "/delete/test", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse deleteTest(@RequestBody MainTestVo MainTestVo) {
        OperationResponse response = new OperationResponse();
        Boolean ret = mainTestService.deleteTest(MainTestVo);
        if (!ret) {
            response.setCode(ErrorCode.CODE_SYSTEM_ERROR);
            response.setMessage(ErrorCode.MSG_SYSTEM_ERROR + "\n删除实验大纲[" + MainTestVo.get_id() + "]出错，请联系开发人员");
        }
        return response;
    }

    @ApiOperation(value = "update a test", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "update a test.", response = OperationResponse.class)})
    @RequestMapping(value = "/update/test", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse updateTest(@RequestBody MainTestVo MainTestVo) {
        OperationResponse response = new OperationResponse();
        try {
            MainTestVo.setUpdateTime(System.currentTimeMillis());
            MainTestVo sv = mainTestService.updateTest(MainTestVo);
            response.setBody(sv);
        } catch (NotFoundException e) {
            response.setCode(ErrorCode.CODE_RECORD_NOT_FOUND);
            response.setMessage(ErrorCode.MSG_RECORD_NOT_FOUND);
        }

        return response;
    }

    @ApiOperation(value = "request a test id", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "request a test id.", response = OperationResponse.class)})
    @RequestMapping(value = "/get/new_test_id", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse getNewTestId(@RequestBody PageParam pageParam) {
        //Example: Ls-18.6.3-1
        String prefix = "Ls";

        Long testStart = (Long) pageParam.getBodyMap().get("testStart");
        testStart = (testStart == null) ? System.currentTimeMillis() : testStart;
        DateTime dt = new DateTime(testStart);
        DateTimeFormatter dtm = DateTimeFormat.forPattern("yyyy.MM.dd");
//        log.info("testStart is " + dtm.print(dt));

        int testNum = mainTestService.getTestNum(testStart) + 1;
        String testId = String.format("%s-%s-%d", prefix, dtm.print(dt), testNum);
        Map<String, String> ret = new HashMap<>(1);
        ret.put("testId", testId);

        OperationResponse response = new OperationResponse();
        response.setBody(ret);
        return response;
    }

}
