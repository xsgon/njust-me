package com.njust.controller;

import com.njust.Exception.AlreadyExistsException;
import com.njust.Exception.NotFoundException;
import com.njust.model.response.ErrorCode;
import com.njust.model.response.OperationResponse;
import com.njust.po.PageParam;
import com.njust.service.PerformService;
import com.njust.vo.PerformTrtVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@Api(tags = {"Sample"})
@RestController
@Slf4j
@RequestMapping(value = "/perform", produces = {"application/json"})
public class PerformController {
    @Autowired
    PerformService performService;

    @ApiOperation(value = "get all trt performance", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Returns all trt performance info.", response = OperationResponse.class)})
    @RequestMapping(value = "/read/all_trt")
    public OperationResponse getAll(@RequestBody PageParam params) {
        params.formalize();
        OperationResponse response = new OperationResponse();

        PageRequest pgReq = params.buildPageRequest("sampleTime");
        Page<PerformTrtVo> pgSamples = performService.findAll(pgReq, new PerformTrtVo());

        response.setPageAndBody(pgSamples);
        return response;
    }

//    @ApiOperation(value = "get all sample in time range", notes = "", response = OperationResponse.class)
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "Returns all samples in time range.", response = OperationResponse.class)})
//    @RequestMapping(value = "/read/all_by_createTime")
//    public OperationResponse getAllByCreateTimeRange(@RequestBody PageParam params) {
//        OperationResponse response = new OperationResponse();
//
//        params.formalize();
//        PageRequest pgReq = params.buildPageRequest("sampleTime");
//        TimeRange tr = null;
//
//        try {
//            tr = (TimeRange) params.getBody();
//        } catch (Exception e) {
//            log.error("cast " + JsonHelper.obj2Json(params.getBody()) + " to TimeRange failed");
//            response.setCode(ErrorCode.CODE_INPUT_PARAMS_ERROR);
//            response.setMessage(ErrorCode.MSG_INPUT_PARAMS_ERROR);
//        }
//
//        if (tr != null) {
//            Page<SampleVo> sampleVos = sampleService.findAllByCreateTimeRange(tr, pgReq);
//            response.setPageAndBody(sampleVos);
//        }
//
//        return response;
//    }

    @ApiOperation(value = "add a new trt performance", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add a new trt performance.", response = OperationResponse.class)})
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    @RequestMapping(value = "/add/trt")
    public OperationResponse addNew(@RequestBody PerformTrtVo trtVo) {
        OperationResponse response = new OperationResponse();
        try {
            trtVo.setCreateTime(System.currentTimeMillis());
            trtVo.setUpdateTime(System.currentTimeMillis());
            PerformTrtVo sv = performService.addNewPerform(trtVo);
            response.setBody(sv);
        } catch (AlreadyExistsException e) {
            response.setCode(ErrorCode.CODE_RECORD_EXISTS);
            response.setMessage(ErrorCode.MSG_RECORD_EXISTS);
        }

        return response;
    }

    @ApiOperation(value = "delete a trt performance", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "delete a trt performance.", response = OperationResponse.class)})
    @RequestMapping(value = "/delete/trt")
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse deletePerform(@RequestBody PerformTrtVo trtVo) {
        OperationResponse response = new OperationResponse();
        Boolean ret = performService.deletePerform(trtVo);
        if (!ret) {
            response.setCode(ErrorCode.CODE_SYSTEM_ERROR);
            response.setMessage("删除样本[" + trtVo.get_id() + "]出错，请联系开发人员");
        }
        return response;
    }

    @ApiOperation(value = "update a trt performance", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "update a trt performance.", response = OperationResponse.class)})
    @RequestMapping(value = "/update/trt")
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse updateSample(@RequestBody PerformTrtVo trtVo) {
        OperationResponse response = new OperationResponse();
        try {
            trtVo.setUpdateTime(System.currentTimeMillis());
            PerformTrtVo pv = performService.updatePerform(trtVo);
            response.setBody(pv);
        } catch (NotFoundException e) {
            response.setCode(ErrorCode.CODE_RECORD_NOT_FOUND);
            response.setMessage(ErrorCode.MSG_RECORD_NOT_FOUND);
        }

        return response;
    }

}
