package com.njust.controller;

import com.njust.Exception.AlreadyExistsException;
import com.njust.Exception.NotFoundException;
import com.njust.model.response.ErrorCode;
import com.njust.model.response.OperationResponse;
import com.njust.po.PageParam;
import com.njust.service.MainTestService;
import com.njust.service.SampleService;
import com.njust.vo.MainTestVo;
import com.njust.vo.SampleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"Common"})
@RestController
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
        Sort.Order timeOrder = new Sort.Order(Sort.Direction.DESC, "createTime");
        Sort sort = new Sort(timeOrder);
        PageRequest pgReq = new PageRequest(params.getPage(), params.getPageSize(), sort);
        Page<SampleVo> pgTests = sampleService.findAll(pgReq);

        List<SampleVo> mainTests = new ArrayList<SampleVo>(pgTests.getContent());
        response.setPageInfo(pgTests);
        response.setBody(mainTests);
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
