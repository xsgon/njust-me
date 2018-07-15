package com.njust.po;

import com.njust.Utils.JsonHelper;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Map;

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

    public Map<String, Object> getBodyMap() {
        return JsonHelper.obj2Map(this.body);
    }

    public PageRequest buildPageRequest(String sortColumn) {
        Sort.Order timeOrder = new Sort.Order(Sort.Direction.DESC, sortColumn);
        Sort sort = new Sort(timeOrder);
        return new PageRequest(this.page, this.pageSize, sort);
    }
}
