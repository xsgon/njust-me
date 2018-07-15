package com.njust.controller;

import com.njust.model.response.OperationResponse;
import com.njust.po.PageParam;
import com.njust.service.MainTestService;
import com.njust.vo.MainTestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = {"Common"})
@RestController
@RequestMapping(value = "/main_test", produces = { "application/json" })
public class MainTestController {
    @Autowired
    MainTestService mainTestService;

  @ApiOperation(value = "get all main test", notes = "", response = OperationResponse.class)
  @ApiResponses(value = { @ApiResponse(code = 200, message = "Returns all test info.", response = OperationResponse.class) })
  @RequestMapping(value="/read/all")
  public OperationResponse getAll(@RequestBody PageParam params) {
      params.formalize();
      OperationResponse response = new OperationResponse();
      PageRequest pgReq = params.buildPageRequest("createTime");
      Page<MainTestVo> pgTests = mainTestService.findAll(pgReq);

      response.setPageAndBody(pgTests);

      return response;
  }


}
