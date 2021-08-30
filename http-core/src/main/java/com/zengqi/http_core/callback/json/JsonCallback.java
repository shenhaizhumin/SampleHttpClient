package com.zengqi.http_core.callback.json;

import static com.zengqi.http_core.Handle_errorsKt.handlingExceptions;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.zengqi.GenericsUtils;
import com.zengqi.http_core.ApiException;
import com.zengqi.http_core.Call;
import com.zengqi.http_core.Callback;
import com.zengqi.http_core.Response;
import com.zengqi.http_core.callback.OnFailed;
import com.zengqi.http_core.callback.OnSuccess;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author zengqi
 * @Date 2021/8/30
 **/
public abstract class JsonCallback<T> implements Callback {
//    private OnSuccess<T> onSuccess;
//    private OnFailed onFailed;
//
//    public JsonCallback(OnSuccess<T> onSuccess, OnFailed onFailed) {
//        this.onSuccess = onSuccess;
//        this.onFailed = onFailed;
//    }

    @Override
    public void onFailure(@Nullable Call call, @Nullable Exception e) {
        Exception error;
        if (e == null) {
            error = new Exception("unknown error!");
        } else {
            error = e;
        }

//        onFailed.onFailed(handlingExceptions(error));
        onFailed(handlingExceptions(error));
    }

    @Override
    public void onResponse(@Nullable Call call, @Nullable Response response) throws IOException {
        Type classType = getClassType(this);
//        Class<?> clazz = GenericsUtils.getSuperClassGenricType(this)
        Gson gson = new Gson();
        T data = gson.fromJson(response.string(), classType);
//        onSuccess.onSuccess(data);
       onSuccess(data);
    }

    public abstract void onSuccess(T data);

    public abstract void onFailed(ApiException apiException);

    //
//        void onFailed(String error);
    private Type getClassType(Object obj) {
        Class<?> aClass = obj.getClass();
        Type type = aClass.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return actualTypeArguments[0];
    }
}
