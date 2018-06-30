package com.njust.model.session;

import com.njust.model.user.Role;
import lombok.*;
import java.util.*;
import io.swagger.annotations.ApiModelProperty;

@Data
public class SessionItem {
    private String  token;
    private String  userId;
    private String  email;
    private String role;
//    private List<String> roles;
}
