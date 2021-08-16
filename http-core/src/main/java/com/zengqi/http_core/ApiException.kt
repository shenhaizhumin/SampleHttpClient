package com.zengqi.http_core

class ApiException : RuntimeException {
    private var code: Int? = null
    var errorCode: Int? = null
    var errorMsg: String? = null

//    constructor(throwable: Throwable, code: Int) : super(throwable) {
//        this.code = code
//    }

    constructor (errorCode: Int?, errorMsg: String?) {
        this.errorCode = errorCode
        this.errorMsg = errorMsg
    }

    constructor(message: String) : super(Throwable(message))

    override fun toString(): String {
        return "ApiException(errorCode=$errorCode, errorMsg=$errorMsg)"
    }
}