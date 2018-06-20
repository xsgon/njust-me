package com.njust.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.njust.model.user.Role;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@Data
public class UserVo {

    @JsonIgnore private String id;
    @JsonIgnore private String name;
    @JsonIgnore private String password;
    @JsonIgnore private String email;
    @JsonIgnore private String portrait;
    @JsonIgnore private String phone;
    @JsonIgnore private Integer active;
    @JsonIgnore private Role role;
    @JsonIgnore private Date createTime;
    @JsonIgnore private Date updateTime;

    public UserVo() {
        this.id = "id";
        this.password = new BCryptPasswordEncoder().encode("test");
    }

}
