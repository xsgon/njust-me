package com.njust.model.response;

public class ErrorCode {
    public static final int CODE_SUCCESS = 200;
    public static final String MSG_SUCCESS = "成功";

    public static final int CODE_SYSTEM_ERROR = 500;
    public static final String MSG_SYSTEM_ERROR = "系统故障";

    public static final int CODE_AUTH_ERROR = 401;
    public static final String MSG_AUTH_ERROR = "认证失败";

    public static final int CODE_ACCESS_DENY = 403;
    public static final String MSG_ACCESS_DENY = "禁止此操作";

    public static final int CODE_ADD_USER_FAILED = 10001;
    public static final String MSG_ADD_USER_FAILED = "添加用户失败";

    public static final int CODE_USER_EXISTS = 10002;
    public static final String MSG_USER_EXISTS = "添加用户失败，用户已存在";

    public static final int CODE_USER_NOT_EXISTS = 10003;
    public static final String MSG_USER_NOT_EXISTS = "用户不存在";

    public static final int CODE_UPDATE_USER_FAILED = 10004;
    public static final String MSG_UPDATE_USER_FAILED = "更新用户失败，请重试";

    public static final int CODE_USER_DISABLED = 10005;
    public static final String MSG_USER_DISABLED = "用户已被禁止访问，请联系管理员";

    public static final int CODE_RECORD_EXISTS = 10006;
    public static final String MSG_RECORD_EXISTS = "记录已存在";

    public static final int CODE_RECORD_NOT_FOUND = 10007;
    public static final String MSG_RECORD_NOT_FOUND = "记录未找到";

    public static final int CODE_INPUT_PARAMS_ERROR = 10008;
    public static final String MSG_INPUT_PARAMS_ERROR = "参数传入错误，请联系开发人员";

    public static final int CODE_DB_SOCKET_TIME_OUT = 20001;
    public static final String MSG_DB_SOCKET_TIME_OUT = "数据库socket连接异常，请重试";



//    public final int CODE_SUCCESS = 200;
//    public final String MSG_SUCCESS = "成功";
//
//    public final int CODE_SUCCESS = 200;
//    public final String MSG_SUCCESS = "成功";
}
