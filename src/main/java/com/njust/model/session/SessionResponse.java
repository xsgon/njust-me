package com.njust.model.session;

import io.swagger.annotations.*;
import lombok.*;
import com.njust.model.response.*;

@Data
@EqualsAndHashCode(callSuper=false)
public class SessionResponse extends OperationResponse {
  @ApiModelProperty(required = true, value = "")
  private SessionItem item;
}
