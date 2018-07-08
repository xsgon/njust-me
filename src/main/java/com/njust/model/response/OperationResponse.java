/**
 * This is the common structure for all responses
 * if the response contains a list(array) then it will have 'items' field
 * if the response contains a single item then it will have 'item'  field
 */


package com.njust.model.response;

import io.swagger.annotations.*;
import lombok.*;
import org.springframework.data.domain.Page;

@Data //for getters and setters
public class OperationResponse {
//  public enum ResponseStatusEnum {SUCCESS, ERROR, WARNING, NO_ACCESS};

    @ApiModelProperty(required = true)
    private int code;
    private String message;
    private long total;
    private int totalPage;
    private int page;
    private int pageSize;
    private Object body;

    public OperationResponse() {
        this.code = ErrorCode.CODE_SUCCESS;
        this.message = ErrorCode.MSG_SUCCESS;
    }

    public void setPageInfo(Page<?> pageInfo) {
        this.total = pageInfo.getTotalElements();
        this.totalPage = pageInfo.getTotalPages();
        this.page = pageInfo.getNumber();
        this.pageSize = pageInfo.getSize();
    }
}
