/**
 * This is the common structure for all responses
 * if the response contains a list(array) then it will have 'items' field
 * if the response contains a single item then it will have 'item'  field
 */


package com.njust.model.response;

import io.swagger.annotations.*;
import lombok.*;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    public void setPageAndBody(Page<?> pageInfo) {
        this.total = pageInfo.getTotalElements();
        this.totalPage = pageInfo.getTotalPages();
        this.page = pageInfo.getNumber();
        this.pageSize = pageInfo.getSize();
        this.body = pageInfo.getContent();
    }

    public void response2Client(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JSONObject.valueToString(this));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
