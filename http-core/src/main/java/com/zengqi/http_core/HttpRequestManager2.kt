package com.zengqi.http_core

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.gson.Gson
import com.zengqi.MyApp
import com.zengqi.api.BaseResponse
import java.lang.Exception
import java.util.concurrent.TimeUnit

internal object HttpRequestManager2 {
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

    inline fun <reified T, reified E> buildRequest(
        req: Request,
        crossinline success: (resp: E) -> Unit,
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
                val resp = gson.fromJson(
                    response?.string(),
                    T::class.java
                )
                resp as BaseResponse<*>
                handlingHttpResponse(resp, {
                    val result: E = if (E::class.java == String::class.java) {
                        gson.toJson(it) as E
                    } else {
                        gson.fromJson(gson.toJson(it), E::class.java)
                    }
                    mHandler.post {
                        success.invoke(result)
                    }
                })

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
        buildRequest<BaseResponse<T>, T>(req, {
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
        buildRequest<BaseResponse<T>, T>(req, {
            success.invoke(it)
        }, {
            failed.invoke(it)
        })
    }


}