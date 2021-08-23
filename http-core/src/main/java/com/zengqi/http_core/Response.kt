package com.zengqi.http_core

import java.io.InputStream
import java.nio.charset.Charset

/**
 * @Author zengqi
 * @Date   2021/8/15
 **/
class Response(
    private val request: Request? = null,
    private val code: Int = 0,
    private val message: String? = null,
//    val handshake: Handshake? = null,
    private val headers: Map<String, String>? = null,
//    private val inputStream: InputStream? = null,
    private val content: String? = null
) {

    fun string(): String {
//        val bufferedReader = inputStream?.bufferedReader(Charset.forName("utf-8"))
//        val sb = StringBuffer()
//        var line = bufferedReader?.readLine()
//        while (!line.isNullOrEmpty()) {
//            sb.append(line)
//            line = bufferedReader?.readLine()
//        }
//        return sb.toString()
        return "$content"
    }

    fun responseMessage(): String? {
        return message
    }

    fun code(): Int {
        return code
    }

    fun getRequest(): Request? {
        return request
    }

    fun getResponseHeaders(): Map<String, String>? {
        return headers
    }
}