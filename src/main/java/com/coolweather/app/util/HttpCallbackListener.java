package com.coolweather.app.util;

/**
 * Created by Administrator on 2016/8/2 0002.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onErroe(Exception e);
}
