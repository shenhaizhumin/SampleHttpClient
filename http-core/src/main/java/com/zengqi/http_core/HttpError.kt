package com.zengqi.http_core

enum class HttpError(val code: Int, val errorMsg: String?) {
    USER_EXIST(20001, "user does not exist"),
    PARAMS_ERROR(20002, "params is error"),
    PARSER_ERROR(500, "数据解析异常"),
    CONNECT_ERROR(-501, "连接异常"),
    CANCELLATION_ERROR(-502, "协程取消异常"),
    UNKNOWN_ERROR(-503, "未知错误"),
    NETWORK_ERROR(-504, "网络异常"),
    UNKNOWN_HOST_ERROR(-505, "域名无法解析"),
    ILLEGAL_ARGUMENT_ERROR(-506, "参数错误"),
    UNKNOWN_SERVICE_ERROR(
        -507,
        "CLEARTEXT communication to targetApplication not permitted by network security policy"
    ),
}