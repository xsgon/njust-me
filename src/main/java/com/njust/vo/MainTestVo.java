package com.njust.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "main_test")
public class MainTestVo extends BaseMongoDbVo{

    private String testId;
    private String testObj;
    private List<String> platformList;
    private List<String> testPeopleList;
    private List<String> sampleList;
    private String testLocation;
    private String testAttachment;
    private Long testStart;
    private Long testEnd;
    private Long createTime;
    private Long updateTime;
}
