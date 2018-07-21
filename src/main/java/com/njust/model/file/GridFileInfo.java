package com.njust.model.file;

import com.mongodb.gridfs.GridFSFile;
import lombok.Data;

import java.util.Date;

@Data
public class GridFileInfo {
    private String id;
    private String fileName;
    private String contentType;
    private Long length;
    private String md5;
    private Date createTime;

    public GridFileInfo(GridFSFile fs) {
        this.id = fs.getId().toString();
        this.fileName = fs.getFilename();
        this.contentType = fs.getContentType();
        this.length = fs.getLength();
        this.md5 = fs.getMD5();
        this.createTime = fs.getUploadDate();
    }
}
