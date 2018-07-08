package com.njust.po;

import lombok.Data;

@Data
public class PageParam {
    private int page;
    private int pageSize;
    private Object body;

    public void formalize() {
        this.page = Math.max(0, this.page);
        this.pageSize = Math.min(1000, this.pageSize);
        if (this.pageSize <= 0) this.pageSize = 100;
    }
}
