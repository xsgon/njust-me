package com.njust.controller;

import com.njust.model.VersionModel;
import com.njust.repo.MainTestRepo;
import com.njust.vo.MainTestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"Common"})
@RestController
@RequestMapping(value = "/main_test", produces = { "application/json" })
public class MainTestController {
    @Autowired
    MainTestRepo mainTestRepo;
  //@ApiOperation(value = "Gets the version of the REST API", notes = "", response = VersionModel.class)
  //@ApiResponses(value = { @ApiResponse(code = 200, message = "Returns the version info for the REST API.", response = VersionModel.class) })
  @RequestMapping(value="/read/all")
  public List<MainTestVo> getVersion() {

//      throw new RuntimeException("time error");
      return mainTestRepo.findAll();
  }
}
