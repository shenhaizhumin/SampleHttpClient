package com.zengqi.http_core

import android.net.ParseException
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonParseException
import com.zengqi.MyApp
import com.zengqi.api.BaseResponse
import org.json.JSONException
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.UnknownServiceException
import java.util.concurrent.CancellationException

/**
 * 处理请求层的错误,对可能的已知的错误进行处理
 */
fun handlingExceptions(e: Throwable): ApiException {
    Log.e("handlingExceptions", "exec:$e")
    when (e) {
        is CancellationException -> {
            return ApiException(
                HttpError.CANCELLATION_ERROR.code,
                HttpError.CANCELLATION_ERROR.errorMsg
            )
        }
        is SocketTimeoutException -> {
            return ApiException(HttpError.CONNECT_ERROR.code, HttpError.CONNECT_ERROR.errorMsg)
        }
        is JsonParseException, is JSONException, is ParseException -> {
            return ApiException(HttpError.PARSER_ERROR.code, HttpError.PARSER_ERROR.errorMsg)
        }
        is UnknownHostException -> {
            return ApiException(
                HttpError.UNKNOWN_HOST_ERROR.code,
                HttpError.UNKNOWN_HOST_ERROR.errorMsg
            )
        }
        is UnknownServiceException -> {
            return ApiException(
                HttpError.UNKNOWN_SERVICE_ERROR.code,
                HttpError.UNKNOWN_SERVICE_ERROR.errorMsg
            )
        }
        is IllegalArgumentException -> {
            return ApiException(
                HttpError.ILLEGAL_ARGUMENT_ERROR.code,
                HttpError.ILLEGAL_ARGUMENT_ERROR.errorMsg
            )
        }
        is HttpException -> {
            val resp = e.resp
            val errorContent = resp?.string()
            return try {
                val jsonObj = JSONObject("$errorContent")
                var msg: String? = jsonObj.getString("msg")
                if (msg == null) {
                    msg = jsonObj.getString("detail")
                }
                val code: Int? = jsonObj.getInt("code")
                ApiException(code ?: -200, msg)
            } catch (t: Exception) {
                ApiException(
                    HttpError.NETWORK_ERROR.code,
                    "${resp?.code()},${resp?.responseMessage()}"
                )
            }
        }
        else -> {
            return ApiException(HttpError.UNKNOWN_ERROR.code, "异常：$e")
        }
    }
}

/**
 * 处理HttpResponse
 * @param res
 * @param successBlock 成功
 * @param failureBlock 失败
 */
fun handlingHttpResponse(
    res: BaseResponse<*>,
    successBlock: (data: Any) -> Unit,
    failureBlock: ((error: ApiException) -> Unit)? = null
) {
    when (res.code) {
        ResponseCode.SUCCESSFUL_CODE -> {
            if (res.data != null) {
                successBlock.invoke(res.data)
            } else {
//                Logger.e("maybe empty,handlingHttpResponse:$res")
            }
        }
//        ResponseCode.ERROR_TOKEN_996, ResponseCode.ERROR_OTHER_DEVICE_LOGIN, ResponseCode.ERROR_TOKEN_997, ResponseCode.ERROR_TOKEN_999, ResponseCode.ERROR_TOKEN_FIELD -> {
//            UserManager.logout()
//        }
        else -> {
            println("handlingHttpResponse:$res")
            val error = ApiException(res.code, res.message)
            failureBlock?.invoke(error) ?: defaultErrorBlock(error)
        }
    }
}

// 默认的处理方案
val defaultErrorBlock: (error: ApiException) -> Unit = { error ->
//    ToastUtils.showShort(error.errorMsg ?: "${error.errorCode}")            // 可以根据是否为debug进行拆分处理
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(MyApp.ctx, error.errorMsg ?: "${error.errorCode}", Toast.LENGTH_SHORT).show()
    }
}
