package com.njust.controller;

import com.njust.Exception.AlreadyExistsException;
import com.njust.Exception.NotFoundException;
import com.njust.Utils.JsonHelper;
import com.njust.model.response.ErrorCode;
import com.njust.model.response.OperationResponse;
import com.njust.po.PageParam;
import com.njust.service.SampleService;
import com.njust.vo.SampleVo;
import com.njust.vo.TimeRangeVo;
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
@RequestMapping(value = "/sample", produces = {"application/json"})
public class SampleController {
    @Autowired
    SampleService sampleService;

    @ApiOperation(value = "get all sample", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Returns all sample info.", response = OperationResponse.class)})
    @RequestMapping(value = "/read/all")
    public OperationResponse getAll(@RequestBody PageParam params) {
        params.formalize();
        OperationResponse response = new OperationResponse();

        PageRequest pgReq = params.buildPageRequest("createTime");
        Page<SampleVo> pgSamples = sampleService.findAll(pgReq);

        response.setPageAndBody(pgSamples);
        return response;
    }

    @ApiOperation(value = "get all sample in time range", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Returns all samples in time range.", response = OperationResponse.class)})
    @RequestMapping(value = "/read/all_by_createTime")
    public OperationResponse getAllByCreateTimeRange(@RequestBody PageParam params) {
        OperationResponse response = new OperationResponse();

        params.formalize();
        PageRequest pgReq = params.buildPageRequest("createTime");
        TimeRangeVo tr = null;

        try {
            tr = (TimeRangeVo) params.getBody();
        } catch (Exception e) {
            log.error("cast " + JsonHelper.obj2Json(params.getBody()) + " to TimeRangeVo failed");
            response.setCode(ErrorCode.CODE_INPUT_PARAMS_ERROR);
            response.setMessage(ErrorCode.MSG_INPUT_PARAMS_ERROR);
        }

        if (tr != null) {
            Page<SampleVo> sampleVos = sampleService.findAllByCreateTimeRange(tr, pgReq);
            response.setPageAndBody(sampleVos);
        }

        return response;
    }

    @ApiOperation(value = "add a new sample", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add a new sample.", response = OperationResponse.class)})
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    @RequestMapping(value = "/add/sample")
    public OperationResponse addNew(@RequestBody SampleVo sampleVo) {
        OperationResponse response = new OperationResponse();
        try {
            sampleVo.setCreateTime(System.currentTimeMillis());
            sampleVo.setUpdateTime(System.currentTimeMillis());
            SampleVo sv = sampleService.addNewSample(sampleVo);
            response.setBody(sv);
        } catch (AlreadyExistsException e) {
            response.setCode(ErrorCode.CODE_RECORD_EXISTS);
            response.setMessage(ErrorCode.MSG_RECORD_EXISTS);
        }

        return response;
    }

    @ApiOperation(value = "delete a sample", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "delete a sample.", response = OperationResponse.class)})
    @RequestMapping(value = "/delete/sample")
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse deleteSample(@RequestBody SampleVo sampleVo) {
        OperationResponse response = new OperationResponse();
        Boolean ret = sampleService.deleteSample(sampleVo);
        if (!ret) {
            response.setCode(ErrorCode.CODE_SYSTEM_ERROR);
            response.setMessage("删除样本[" + sampleVo.get_id() + "]出错，请联系开发人员");
        }
        return response;
    }

    @ApiOperation(value = "update a sample", notes = "", response = OperationResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "update a sample.", response = OperationResponse.class)})
    @RequestMapping(value = "/update/sample")
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse updateSample(@RequestBody SampleVo sampleVo) {
        OperationResponse response = new OperationResponse();
        try {
            sampleVo.setUpdateTime(System.currentTimeMillis());
            SampleVo sv = sampleService.updateSample(sampleVo);
            response.setBody(sv);
        } catch (NotFoundException e) {
            response.setCode(ErrorCode.CODE_RECORD_NOT_FOUND);
            response.setMessage(ErrorCode.MSG_RECORD_NOT_FOUND);
        }

        return response;
    }

}
