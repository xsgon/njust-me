package com.njust.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import com.njust.model.VersionModel;

@RestController
@RequestMapping(value = "/version", produces = { "application/json" })
@Api(tags = {"Common"})
public class Version {
  @ApiOperation(value = "Gets the version of the REST API", notes = "", response = VersionModel.class)
  @ApiResponses(value = { @ApiResponse(code = 200, message = "Returns the version info for the REST API.", response = VersionModel.class) })
  @RequestMapping( method = RequestMethod.GET)
  public VersionModel getVersion() {

      VersionModel r = new VersionModel();
      r.setVersion("1.0.0");
      r.setMajor(1);
      r.setMinor(0);
      r.setPatch(0);

//      throw new RuntimeException("time error");
      return r;
  }
}
