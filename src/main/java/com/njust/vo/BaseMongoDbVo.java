package com.njust.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class BaseMongoDbVo {
    @Id
    private String _id;
}
