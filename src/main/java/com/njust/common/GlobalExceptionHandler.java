package com.njust.common;

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
        resp.setCode(ErrorCode.CODE_SYSTEM_ERROR);
        resp.setMessage(e.getMessage());
//        resp.setOperationStatus(ResponseStatusEnum.ERROR);
//        resp.setOperationMessage(e.getMessage());
        return resp;
    }

}
