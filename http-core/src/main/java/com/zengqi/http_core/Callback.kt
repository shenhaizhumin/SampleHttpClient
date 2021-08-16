package com.zengqi.http_core

import java.io.IOException
import java.lang.Exception

/**
 * @Author zengqi
 * @Date   2021/8/15
 **/
open interface Callback {
    fun onFailure(call: Call?, e: Exception?)

    @Throws(IOException::class)
    fun onResponse(call: Call?, response: Response?)
}