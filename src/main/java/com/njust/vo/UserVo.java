package com.njust.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.njust.model.user.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
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
    private Long createTime;    // system mill seconds
    private Long updateTime;

    public UserVo() {
        this.id = "id";
        this.password = new BCryptPasswordEncoder().encode("test");
    }

    public void setRole(String role) {
        for (Role r : Role.values()) {
            if (r.name().equals(role)) {
                this.role = role;
                return;
            }
        }

        this.role = Role.ROLE_NORMAL.name();
    }

    public void hideSensitiveInfo() {
        this.setPassword("$hidden*");
        this.set_id("$hidden*");
    }

}
