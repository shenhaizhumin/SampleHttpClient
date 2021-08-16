package com.zengqi.http_core

/**
 * @Author zengqi
 * @Date   2021/8/15
 **/
interface Call : Cloneable {
    fun request(): Request?


    fun enqueue(responseCallback: Callback)

    fun cancel()


    val isExecuted: Boolean
    val isCanceled: Boolean


    public override fun clone(): Call
    interface Factory {
        fun newCall(request: Request?): Call?
    }
}
