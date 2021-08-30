package com.zengqi.http_core;

import android.os.Handler;
import android.os.Looper;

import com.zengqi.GenericsUtils;
import com.zengqi.http_core.callback.OnFailed;
import com.zengqi.http_core.callback.OnSuccess;
import com.zengqi.http_core.callback.json.JsonCallback;

import java.util.HashMap;
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

//    public <T> void get(String path, HashMap<String, String> params, OnSuccess<T> success, OnFailed failed) {
//        Request req = new Request.Builder()
//                .setQueryParams(params)
//                .url(baseUrl + path)
//                .build();
//        httpClient.newCall(req).enqueue(new JsonCallback<T>(success, failed));
//    }

//    public <T> void get(String path, HashMap<String, String> params, OnSuccess<T> onSuccess, OnFailed onFailed) {
//        Request req = new Request.Builder()
//                .setQueryParams(params)
//                .url(baseUrl + path)
//                .build();
//        GenericsUtils.getSuperClassGenricType(onSuccess.getClass());
//        httpClient.newCall(req).enqueue(new JsonCallback<T>() {
//            @Override
//            public void onSuccess(T data) {
//                handler.post(() -> onSuccess.onSuccess(data));
//            }
//
//            @Override
//            public void onFailed(ApiException apiException) {
//                handler.post(() -> onFailed.onFailed(apiException));
//            }
//        });
//    }

    public <T> void get(String path, HashMap<String, String> params, JsonCallback<T> jsonCallback) {
        Request req = new Request.Builder()
                .setQueryParams(params)
                .url(baseUrl + path)
                .build();
        httpClient.newCall(req).enqueue(jsonCallback);
    }

    public <T> void get(String path, HashMap<String, String> params, OnSuccess<T> success, OnFailed failed) {
        get(path, params, new JsonCallback<T>() {
            @Override
            public void onSuccess(T data) {
                success.onSuccess(data);
            }

            @Override
            public void onFailed(ApiException apiException) {
                failed.onFailed(apiException);
            }
        });
    }
}
