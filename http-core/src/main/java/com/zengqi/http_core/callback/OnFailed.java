package com.zengqi.http_core.callback;

import com.zengqi.http_core.ApiException;

/**
 * @Author zengqi
 * @Date 2021/8/30
 **/
public interface OnFailed {
    void onFailed(ApiException apiException);
}
