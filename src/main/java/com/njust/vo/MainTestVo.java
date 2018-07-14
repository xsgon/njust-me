package com.njust.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "main_test")
public class MainTestVo {
    /*
    sid	test_id	test_obj	samp_num	samp_id	test_start
    test_end	perf_id	test_eqp
    test_ppl	test_loc	test_data	test_concl

    {
	"_id" : ObjectId("5b27acb43d1752cacc280e81"),
	"test_id" : "LS-X001-18.06.03-001",
	"test_obj" : "测试目的",
	"samp_num" : 2,
	"samp_id" : "",
	"test_start" : 1528030605000,
	"test_end" : 1529326605000,
	"perf_id" : ""
}

     */
    @Id
    private String _id;

    private String testId;
    private String testObj;
    private Integer sampNum;
    private List<String> sampId;
    private Long testStart;
    private Long testEnd;
    private List<String> perfId;
}
