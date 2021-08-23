package com.zengqi.http_core

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.zengqi.MyApp
import java.util.concurrent.TimeUnit

object HttpRequestManager {
//    const val BASE_URL = ""

    var baseUrl = "http://192.168.4.133:8000"

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

    inline fun <reified T> buildRequest(
        req: Request,
        crossinline success: (resp: T) -> Unit,
        crossinline failed: (apiExec: ApiException) -> Unit
    ) {
        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: Exception?) {
                val apiException = handlingExceptions(e!!)
                mHandler.post {
                    failed.invoke(apiException)
                    Toast.makeText(MyApp.ctx, "${apiException.errorMsg}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                val resp = if (T::class.java == String::class.java) {
                    response?.string() as T
                } else {

//                    val jsonReader =
//                        gson.newJsonReader(response?.string()?.byteInputStream()?.reader())
//                    val adapter: TypeAdapter<T> = gson.getAdapter(TypeToken.get(T::class.java))
//                    val result: T = adapter.read(jsonReader)
//                    result
                    val type = TypeToken.get(T::class.java).type
                    val result: T = gson.fromJson(
                        response?.string(),
                        type
                    )
                    result
                }

                mHandler.post {
                    success.invoke(resp)
                }

            }
        })
    }

    inline fun <reified T> get(
        path: String,
        params: HashMap<String, String>,
        crossinline success: (data: T) -> Unit = {},
        crossinline failed: (error: ApiException) -> Unit = {}
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

    inline fun <reified T> postForm(
        path: String,
        params: HashMap<String, String>,
        crossinline success: (data: T) -> Unit = {},
        crossinline failed: (error: ApiException) -> Unit = {}
    ) {
        post<T>(path, params, true, success, failed)
    }

    inline fun <reified T> postJson(
        path: String,
        params: HashMap<String, String>,
        crossinline success: (data: T) -> Unit = {},
        crossinline failed: (error: ApiException) -> Unit = {}
    ) {
        post<T>(path, params, false, success, failed)
    }

    inline fun <reified T> post(
        path: String,
        params: HashMap<String, String>,
        isForm: Boolean,
        crossinline success: (data: T) -> Unit = {},
        crossinline failed: (error: ApiException) -> Unit = {}
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

//inline fun <reified T> Response.parseArrayEX() =
//    try {
//        Gson().fromJson<List<T>>(data, ParameterizedTypeImpl(T::class.java))
//    } catch (e: Exception) {
//        e.printStackTrace()
//        mutableListOf<T>()
//    }
//
////返回空列表替代null
//inline fun <reified T> Response.parseArrayEXV2() =
//    try {
//        //如果没有data这个字段，会返回null
//        Gson().fromJson<List<T>>(data, ParameterizedTypeImpl(T::class.java)) ?: mutableListOf<T>()
//    } catch (e: Exception) {
//        e.printStackTrace()
//        mutableListOf<T>()
//    }
//
//inline fun <reified T> Response.parseDataEx() =
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
