package com.zengqi

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApp:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var ctx:Context
    }
    override fun onCreate() {
        super.onCreate()
        ctx=this
    }
}