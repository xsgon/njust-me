package com.njust.model.session;

import com.njust.model.user.Role;
import com.njust.vo.UserVo;
import lombok.*;
import java.util.*;
import io.swagger.annotations.ApiModelProperty;

@Data
public class SessionItem {
    private String token;
    private UserVo user;
}
