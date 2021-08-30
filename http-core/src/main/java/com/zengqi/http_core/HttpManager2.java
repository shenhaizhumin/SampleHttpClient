package com.zengqi.http_core;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.zengqi.GenericsUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author zengqi
 * @Date 2021/8/30
 **/
public class HttpManager2 {
    String baseUrl = "http://192.168.3.178:8000";
    private static HttpClient httpClient;
    private static Handler handler = new Handler(Looper.getMainLooper());

    private HttpManager2() {
        httpClient = new HttpClient.Builder()
                .readTimeOut(3000, TimeUnit.SECONDS)
                .connectTimeOut(3000, TimeUnit.SECONDS)
                .writeTimeOut(3000, TimeUnit.SECONDS)
                .build();
    }

    public static HttpManager2 getInstance() {
        return Holder.httpManager2();
    }

    public static class Holder {
        private static HttpManager2 httpManager2() {
            return new HttpManager2();
        }
    }

    public  <T> void get(String path, HashMap<String, String> params, HttpCallback<T> callback) {
        Request req = new Request.Builder()
                .setQueryParams(params)
                .url(baseUrl+path)
                .build();
        httpClient.newCall(req).enqueue(callback);
    }




}
