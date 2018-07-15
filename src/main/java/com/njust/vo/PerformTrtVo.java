package com.njust.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "sample_perform_trt")
public class PerformTrtVo extends BaseMongoDbVo{

    private String testId;
    private String sampleId;
    private Float testTemperature;
    private Float testHum;
    private Float testV;
    private Float testA;
    private Float testRev;
    private Float testStroke;
    private String testInstallAcc;
    private String testEqp;
    private String testPt;
    private Float min0;
    private Float min5;
    private Float min10;
    private Float min15;
    private Float min20;
    private Float min25;
    private Float min30;
    private Float min35;
    private Float min40;
    private Float min45;
    private Float min50;
    private Float min55;
    private Float min60;
    private String attachment;

    private Long createTime;
    private Long updateTime;
}
