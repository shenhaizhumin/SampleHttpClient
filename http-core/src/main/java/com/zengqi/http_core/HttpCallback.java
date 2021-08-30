package com.zengqi.http_core;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author zengqi
 * @Date 2021/8/30
 **/
public abstract class HttpCallback<T> implements com.zengqi.http_core.Callback {
    @Override
    public void onFailure(@Nullable Call call, @Nullable Exception e) {

    }

    @Override
    public void onResponse(@Nullable Call call, @Nullable Response response) throws IOException {
        Type classType = getClassType(this);
        System.out.println(classType);
           onSuccess(new Gson().fromJson(response.string(),classType));
    }
    public abstract void onSuccess(T data);
//
//        void onFailed(String error);
private  Type getClassType(Object obj) {
    Class<?> aClass = obj.getClass();
    Type type = aClass.getGenericSuperclass();
    ParameterizedType parameterizedType = (ParameterizedType) type;
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
    return actualTypeArguments[0];
}
}
