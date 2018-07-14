package com.njust.common;

import com.mongodb.MongoSocketReadTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.njust.model.response.*;

/*
@ControllerAdvice tells your spring application that this class will do the exception handling for your application.
@RestController will make it a controller and let this class render the response.
Use @ExceptionHandler annotation to define the class of Exception it will catch. (A Base class will catch all the Inherited and extended classes)
*/
@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {
//        @ExceptionHandler(value = DataIntegrityViolationException.class)
//    public OperationResponse handleBaseException(DataIntegrityViolationException e){
//        OperationResponse resp = new OperationResponse();
//        resp.setOperationStatus(ResponseStatusEnum.ERROR);
//        resp.setOperationMessage(e.getRootCause().getMessage());
//        return resp;
//    }
    @ExceptionHandler()
    public OperationResponse handleBaseException(Exception e) {
        OperationResponse resp = new OperationResponse();

        if (e instanceof AccessDeniedException) {
            resp.setCode(ErrorCode.CODE_ACCESS_DENY);
            resp.setMessage(ErrorCode.MSG_ACCESS_DENY);
        } else if (e instanceof MongoSocketReadTimeoutException) {
            resp.setCode(ErrorCode.CODE_DB_SOCKET_TIME_OUT);
            resp.setMessage(ErrorCode.MSG_DB_SOCKET_TIME_OUT);
        } else {
            resp.setCode(ErrorCode.CODE_SYSTEM_ERROR);
            resp.setMessage(e.getMessage());

            log.error("exception", e);
        }
//        resp.setOperationStatus(ResponseStatusEnum.ERROR);
//        resp.setOperationMessage(e.getMessage());
        return resp;
    }

}
