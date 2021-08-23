package com.zengqi.http_core;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zengqi
 * @Date 2021/8/15
 **/
public class FormBody extends RequestBody {
    //    private Map<String, Object> params = new HashMap<>();
    public List<String> names;
    public List<String> values;

    public FormBody(List<String> names, List<String> values) {
        this.names = names;
        this.values = values;
    }

    public String writeTo() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, size = names.size(); i < size; i++) {
            if (i > 0) sb.append('&');
            sb.append(names.get(i));
            sb.append('=');
            sb.append(values.get(i));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "FormBody{" +
                "names=" + names +
                ", values=" + values +
                '}';
    }

    @NotNull
    @Override
    public byte[] writeToBuffer() {
        return writeTo().getBytes();
    }


    public static final class Builder {
        private List<String> names = new ArrayList<>();
        private List<String> values = new ArrayList<>();

        public Builder add(String var1, String var2) {
            names.add(var1);
            values.add(var2);
            return this;
        }

        public Builder setMapParams(HashMap<String, String> params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                names.add(entry.getKey());
                values.add(entry.getValue());
            }
            return this;
        }

        public FormBody build() {
            return new FormBody(names, values);
        }
    }


}
