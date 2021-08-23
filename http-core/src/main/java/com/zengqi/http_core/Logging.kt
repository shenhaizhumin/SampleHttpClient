package com.zengqi.http_core

import android.util.Log

object Logging {
    private const val TAG = "my-logger"
    fun info(msg: String) {
        Log.i(TAG, msg)
    }

    fun error(msg: String) {
        Log.e(TAG, msg)
    }
}