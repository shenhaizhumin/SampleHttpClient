package com.zengqi.http_core

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * @Author zengqi
 * @Date   2021/8/15
 **/
class RealCall(
    private val client: HttpClient,
    private val originalRequest: Request,
//    private val callback: Callback
) : Call, Runnable {
    var conn: HttpURLConnection? = null
    private lateinit var callback: Callback
    override val isExecuted: Boolean
        get() {
            return false
        }
    override val isCanceled: Boolean
        get() {
            return false
        }
    private var req: Request? = null
    override fun request(): Request {
        return originalRequest
    }

    override fun enqueue(responseCallback: Callback) {
        callback = responseCallback
        client.dispatcher().addTask(this)
    }

    override fun cancel() {
    }

    override fun clone(): Call {
        return this
    }


    companion object {
        fun newRealCall(
            client: HttpClient,
            originalRequest: Request
        ): RealCall {
            // Safely publish the Call instance to the EventListener.
            //        call.transmitter = Transmitter(client, call)
            return RealCall(client, originalRequest)
        }
    }


    override fun run() {
        var url = StringBuffer(originalRequest.url!!)
        val method = originalRequest.method
        val headers = originalRequest.headers
        val body = originalRequest.body
        val params = originalRequest.params
        Logging.info("====>request started: $method $url")
        Logging.info("request params：${params?.toString() ?: body.toString()}")
        try {
            if (!params.isNullOrEmpty()) {
                url.append("?")
                for (k in params.keys) {
                    url.append(k)
                    url.append("=")
                    url.append(params[k])
                    url.append("&")
                }
                if (url.endsWith("&")) {
                    url.deleteCharAt(url.length - 1)
                }
            }
            conn = URL(url.toString()).openConnection() as HttpURLConnection
            //设置超时时间
            conn?.readTimeout = client.readTimeOut()?.timeOutValue!!
            conn?.connectTimeout = client.connectTimeOut()?.timeOutValue!!
            if (!headers.isNullOrEmpty()) {
                for (k in headers.keys) {
                    conn?.setRequestProperty(k, headers[k])
                }
            }
            conn?.requestMethod = method?.uppercase()
            if (method?.lowercase() != "get") {
                if (body != null) {
                    if (body is FormBody) {
                        conn?.setRequestProperty(
                            "Content-Type",
                            "application/x-www-form-urlencoded; charset=UTF-8"
                        )
                        conn?.outputStream?.write(body.writeToBuffer())
                    } else if (body is JsonBody) {
                        conn?.setRequestProperty("Content-Type", "application/json")
                        conn?.outputStream?.write(
                            body.writeToBuffer()
                        )
                    }
                    conn?.outputStream?.flush()
                    conn?.outputStream?.close()
                }
            }
            Logging.info("====>request ended: $method $url ${conn?.responseCode} ${conn?.responseMessage}")
            if (conn?.responseCode == 200) {
                val content = streamToStr(conn?.inputStream)
                val resp = Response(
                    originalRequest,
                    conn?.responseCode!!,
                    conn?.responseMessage,
                    null,
                    content
                )
                Logging.info("response: $content")
                callback.onResponse(this, resp)
            } else {
                val content = streamToStr(conn?.errorStream)
                val resp = Response(
                    originalRequest,
                    conn?.responseCode!!,
                    conn?.responseMessage,
                    null,
                    content
                )
                Logging.info("error response: $content")
                callback.onFailure(this, HttpException(resp))
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            callback.onFailure(this, Exception(t))
            Logging.error("error: $t")
        } finally {
            conn?.disconnect()
        }
    }

    private fun streamToStr(inputStream: InputStream?): String? {
        if (inputStream == null) return null
        val bufferedReader = inputStream.bufferedReader()
        var sb = StringBuffer()
        var line = bufferedReader.readLine()
        while (line != null) {
            sb.append(line)
            line = bufferedReader.readLine()
        }
        bufferedReader.close()
        return sb.toString()
    }

    private fun cloneInputStream(input: InputStream?): ByteArrayOutputStream? {
        if (input == null) return null
        return try {
            val baos = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int
            while (input.read(buffer).also { len = it } > -1) {
                baos.write(buffer, 0, len)
            }
            baos.flush()
            baos
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}