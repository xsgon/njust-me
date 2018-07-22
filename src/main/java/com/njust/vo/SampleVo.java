package com.njust.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "sample")
public class SampleVo extends BaseMongoDbVo {

    private String testId;
    private String sampleId;
    private Long sampleTime;
    private String sampleWay;
    private String companyName;
    private String modelName;
    private Float threadLen;
    private Float wholeLen;
    private Integer d0;
    private Float ph0;
    private Float d1;
    private Float dw;
    private Float d2;
    private Integer ballCircle;
    private Integer ballList;
    private Float beta;
    private Float r;
    private Float alfa;
    private Float nutDg6;
    private Float nutD1;
    private Float nutB;
    private Float nutL1;
    private Float nutM;
    private Float ca;
    private Float coa;
    private Integer k;
    private Integer dn0;
    private String cycleModel;
    private String nutType;
    private Boolean preload;
    private Boolean selfLub;
    private String special;

    private Long createTime;
    private Long updateTime;
}
