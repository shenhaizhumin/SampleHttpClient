package com.zengqi.http_core

import android.os.Handler
import android.os.Looper
import java.util.concurrent.TimeUnit

/**
 * @Author zengqi
 * @Date   2021/8/15
 **/
class HttpClient {
    private var dispatcher: Dispatcher = Dispatcher()
    val mHandler by lazy {
        Handler(Looper.getMainLooper())
    }
    private var readTimeOut: Timeout? = null
    private var writeTimeOut: Timeout? = null
    private var connectTimeOut: Timeout? = null


    fun dispatcher(): Dispatcher {
        return dispatcher
    }

    fun newCall(request: Request): Call {

        return RealCall.newRealCall(this, request)
    }

    constructor(builder: Builder) {
        readTimeOut = builder.getReadTimeOut()
        writeTimeOut = builder.getWriteTimeOut()
        connectTimeOut = builder.getConnectTimeOut()
    }

    internal fun readTimeOut() = readTimeOut
    internal fun connectTimeOut() = connectTimeOut

    class Builder {
        private var readTimeOut: Timeout = Timeout()
        private var writeTimeOut: Timeout = Timeout()
        private var connectTimeOut: Timeout = Timeout()

        fun readTimeOut(readTimeOut: Int, timeUnit: TimeUnit): Builder {
            this.readTimeOut = Timeout(readTimeOut, timeUnit)
            return this
        }

        internal fun getReadTimeOut() = readTimeOut
        internal fun getWriteTimeOut() = writeTimeOut
        internal fun getConnectTimeOut() = connectTimeOut

        fun writeTimeOut(writeTimeOut: Int, timeUnit: TimeUnit): Builder {
            this.writeTimeOut = Timeout(writeTimeOut, timeUnit)
            return this
        }

        fun connectTimeOut(connectTimeOut: Int, timeUnit: TimeUnit): Builder {
            this.connectTimeOut = Timeout(connectTimeOut, timeUnit)
            return this
        }

        fun build() = HttpClient(this)
    }

}