package com.zengqi.http_core

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.zengqi.MyApp
import com.zengqi.http_core.callback.json.JsonCallback
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

object HttpRequestManager {
//    const val BASE_URL = ""

    var baseUrl = "http://192.168.3.178:8000"

    /**
     * 初始化base_url
     */
    fun init(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    val mHandler by lazy {
        Handler(Looper.getMainLooper())
    }
    val gson by lazy {
        Gson()
    }
    var client: HttpClient = HttpClient.Builder()
        .readTimeOut(3000, TimeUnit.SECONDS)
        .connectTimeOut(3000, TimeUnit.SECONDS)
        .writeTimeOut(3000, TimeUnit.SECONDS)
        .build()

    private fun <T> buildRequest(
        req: Request,
        success: (resp: T) -> Unit,
        failed: (apiExec: ApiException) -> Unit
    ) {
//        client.newCall(req).enqueue(object : JsonCallback<T>() {
//            override fun onSuccess(data: T) {
//                mHandler.post { success.invoke(data) }
//            }
//
//            override fun onFailed(apiException: ApiException) {
//                mHandler.post {
//                    failed.invoke(apiException)
//                    Toast.makeText(MyApp.ctx, "${apiException.errorMsg}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
    }

    fun <T> get(
        path: String,
        params: HashMap<String, String>,
        success: (data: T) -> Unit = {},
        failed: (error: ApiException) -> Unit = {}
    ) {
        val req = Request.Builder()
            .setQueryParams(params)
            .url("$baseUrl$path")
            .build()
        buildRequest<T>(req, {
            success.invoke(it)
        }, {
            failed.invoke(it)
        })
    }

    fun <T> postForm(
        path: String,
        params: HashMap<String, String>,
        success: (data: T) -> Unit = {},
        failed: (error: ApiException) -> Unit = {}
    ) {
        post<T>(path, params, true, success, failed)
    }

    fun <T> postJson(
        path: String,
        params: HashMap<String, String>,
        success: (data: T) -> Unit = {},
        failed: (error: ApiException) -> Unit = {}
    ) {
        post<T>(path, params, false, success, failed)
    }

    fun <T> post(
        path: String,
        params: HashMap<String, String>,
        isForm: Boolean,
        success: (data: T) -> Unit = {},
        failed: (error: ApiException) -> Unit = {}
    ) {
        val body = if (isForm) {
            FormBody.Builder()
                .setMapParams(params)
                .build()
        } else {
            JsonBody.Builder()
                .setMapParams(params)
                .build()
        }

        val req = Request.Builder()
            .post(body)
            .url("$baseUrl$path")
            .build()
        buildRequest<T>(req, {
            success.invoke(it)
        }, {
            failed.invoke(it)
        })
    }
}

// fun < T> Response.parseArrayEX() =
//    try {
//        Gson().fromJson<List<T>>(data, ParameterizedTypeImpl(T::class.java))
//    } catch (e: Exception) {
//        e.printStackTrace()
//        mutableListOf<T>()
//    }
//
////返回空列表替代null
// fun < T> Response.parseArrayEXV2() =
//    try {
//        //如果没有data这个字段，会返回null
//        Gson().fromJson<List<T>>(data, ParameterizedTypeImpl(T::class.java)) ?: mutableListOf<T>()
//    } catch (e: Exception) {
//        e.printStackTrace()
//        mutableListOf<T>()
//    }
//
// fun < T> Response.parseDataEx() =
//    try {
//        Gson().fromJson<T>(data, T::class.java)
//    } catch (e: Exception) {
//        CrashReport.postCatchedException(
//            ResponseParseException(
//                "数据解析 code:${code},msg:${msg},eMessage:${e.message},data:${data}",
//                e
//            )
//        )
//        e.printStackTrace()
//        T::class.java.newInstance()
//    }
//private fun <T> Gson.fromJson(string: String?, type: TypeToken<T>?): T {
//    return this.fromJson(string, type)
//}
