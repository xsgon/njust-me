package com.njust.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.njust.model.user.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@Data
@Document(collection = "users")
public class UserVo {

    @Id
    private String _id;

    private String id;

    private String name;
    private String password;
    private String email;
    private String portrait;
    private String phone;
    private Integer active;
    private String role;
    private Date createTime;
    private Date updateTime;

    public UserVo() {
        this.id = "id";
        this.password = new BCryptPasswordEncoder().encode("test");
    }

}
