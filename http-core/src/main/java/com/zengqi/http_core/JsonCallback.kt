package com.zengqi.http_core

import com.google.gson.Gson
import com.zengqi.GenericsUtils
import java.lang.Exception

/**
 * @Author zengqi
 * @Date   2021/8/30
 **/
class JsonCallback<T>(
    private val onSuccess: (data: T) -> Unit,
    private val onFailed: (apiError: ApiException) -> Unit
) : Callback {

    override fun onResponse(call: Call?, response: Response?) {
        val clazz = GenericsUtils.getSuperClassGenricType(this::class.java)
        val gson = Gson()
        val data = gson.fromJson<T>(response?.string(), clazz)
        onSuccess(data)
    }

    override fun onFailure(call: Call?, e: Exception?) {
        onFailed(handlingExceptions(e ?: Exception("unKnown error!")))
    }
}