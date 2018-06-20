package com.njust.model.user;

import com.njust.vo.UserVo;
import io.swagger.annotations.*;
import lombok.*;
import com.njust.model.response.*;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserResponse extends OperationResponse {
    private UserVo data = new UserVo();
}
