package com.njust.controller;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.njust.Utils.FileTypeHelper;
import com.njust.model.file.GridFileInfo;
import com.njust.model.response.ErrorCode;
import com.njust.model.response.OperationResponse;
import com.njust.po.PageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

@Api(tags = {"Common"})
@RestController
@RequestMapping(value = "/file")
@Slf4j
public class FileStorageController {
    @Autowired
    GridFsTemplate gridFsTemplate;

    @ApiOperation(value = "upload a file", notes = "", response = OperationResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "upload a file.", response = OperationResponse.class) })
    @RequestMapping(value="/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse upload(@RequestParam("file") MultipartFile file) {
        InputStream inputStream = null;
        OperationResponse operationResponse = new OperationResponse();

        try {
            inputStream = file.getInputStream();
            String ext = (file.getOriginalFilename() == null) ?
                    "" : StringUtils.getFilenameExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + (StringUtils.isEmpty(ext) ? "" : "." + ext);
            GridFSFile fs = gridFsTemplate.store(inputStream, fileName);
            operationResponse.setBody(new GridFileInfo(fs));
        } catch (IOException e) {
            log.error("upload exception", e);
            operationResponse.setCode(ErrorCode.CODE_IO_ERROR);
            operationResponse.setMessage(ErrorCode.MSG_IO_ERROR + "\n" + e.getMessage());
        }

        return operationResponse;
    }

    @ApiOperation(value = "preview a file", notes = "", response = OperationResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "preview a file.", response = OperationResponse.class) })
    @RequestMapping(value="/preview", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void preview(@RequestParam("filename") String fileName, HttpServletResponse response) throws IOException, ServletException {
        GridFSDBFile fs = gridFsTemplate.findOne(
                new Query().addCriteria(Criteria.where("filename").is(fileName)));
        boolean previewed = false;
        if (fs != null) {
            if (FileTypeHelper.isPicture(fileName) || FileTypeHelper.isPdf(fileName)) {
                previewed = previewSimpleFile(fs, response);
            }
        }

        if (!previewed) {
            OperationResponse rsp = new OperationResponse();
            rsp.setCode(ErrorCode.CODE_PREVIEW_NOT_SUPPORTED);
            rsp.setMessage(ErrorCode.MSG_PREVIEW_NOT_SUPPORTED);
            rsp.response2Client(response);
        }
    }

    @ApiOperation(value = "download a file", notes = "", response = OperationResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "download a file.", response = OperationResponse.class) })
    @RequestMapping(value="/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void download(@RequestParam(value = "filename", required = true) String fileName,
                         HttpServletResponse response) throws IOException {

        GridFSDBFile fs = gridFsTemplate.findOne(
                new Query().addCriteria(Criteria.where("filename").is(fileName)));

        if (fs != null) {
            IOUtils.copy(fs.getInputStream(), response.getOutputStream());
        } else {
            OperationResponse rsp = new OperationResponse();
            rsp.setCode(ErrorCode.CODE_FILE_NOT_EXISTS);
            rsp.setMessage(ErrorCode.MSG_FILE_NOT_EXISTS);
            rsp.response2Client(response);
        }
    }

    @ApiOperation(value = "delete a file", notes = "", response = OperationResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "delete a file.", response = OperationResponse.class) })
    @RequestMapping(value="/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN", "SUPER_ADMIN"})
    public OperationResponse delete(@RequestBody PageParam param) {
        String fileName = (String) param.getBodyMap().get("fileName");
        if (!StringUtils.isEmpty(fileName)) {
            gridFsTemplate.delete(new Query().addCriteria(Criteria.where("filename").is(fileName)));
        }

        return new OperationResponse();
    }

    private boolean previewSimpleFile(GridFSDBFile fs, HttpServletResponse response) {
        boolean success = true;
        try {
            BufferedInputStream br = new BufferedInputStream(fs.getInputStream());
            response.setHeader("Content-Disposition",
                    "inline; filename=" + URLEncoder.encode(fs.getFilename(), "UTF-8"));
            OutputStream out = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = br.read(buf)) != -1)
                out.write(buf, 0, len);
            br.close();
            out.close();
        } catch (Exception e) {
            log.error("previewSimpleFile", e);
            success = false;
        }

        return success;
    }

    private void previewExcel(GridFSDBFile fs, HttpServletResponse response) {
//        boolean success = true;
//        try {
//            HSSFWorkbook workBook = new HSSFWorkbook(fs.getInputStream());
//            ExcelToHtmlConverter converter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
//            converter.setOutputColumnHeaders(false);// 不显示列的表头
//            converter.setOutputRowNumbers(false);// 不显示行的表头
//            converter.processWorkbook(workBook);
//            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//            StreamResult streamResult = new StreamResult(outStream);
//            Transformer serializer = TransformerFactory.newInstance().newTransformer();
//            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//            serializer.setOutputProperty(OutputKeys.METHOD, "html");
//            serializer.transform(new DOMSource(converter.getDocument()),
//                    streamResult);
//            response.getOutputStream().write(outStream.toByteArray());
//        } catch (Exception e) {
//            log.error("previewExcel", e);
//            success = false;
//        }
    }
}
