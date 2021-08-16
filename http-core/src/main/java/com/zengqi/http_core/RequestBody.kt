package com.zengqi.http_core

/**
 * @Author zengqi
 * @Date   2021/8/15
 **/
abstract class RequestBody {
    //    abstract fun create(contentType: String)
    abstract fun writeToBuffer(): ByteArray
}