package com.zengqi.http_core;

import com.google.gson.Gson;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GsonUtils {
    private static final Gson gson = new Gson();

    //    public static <T> Result<List<T>> fromJsonArray(Reader reader, Class<T> clazz) {
//        Type type = TypeBuilder
//                .newInstance(Result.class)
//                .beginSubType(List.class)
//                .addTypeParam(clazz)
//                .endSubType()
//                .build();
//        return gson.fromJson(reader, type);
//    }
//
//    public static <T> Result<T> fromJsonObject(Reader reader, Class<T> clazz) {
//        Type type = TypeBuilder
//                .newInstance(Result.class)
//                .addTypeParam(clazz)
//                .build();
//        return GSON.fromJson(reader, type);
//    }
//    public static <T> T fromJsonObject(Reader reader, Class<T> clazz) {
//        Type type = new ParameterizedTypeImpl(clazz, new Class[]{clazz});
//        return gson.fromJson(reader, type);
//    }
//    public static <T> List<T> fromJsonArray(Reader reader, Class<T> clazz) {
//        // 生成List<T> 中的 List<T>
//        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
//        // 根据List<T>生成完整的Result<List<T>>
//        Type type = new ParameterizedTypeImpl(List.class, new Type[]{listType});
//        return gson.fromJson(reader, type);
//    }

    public static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;

        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
