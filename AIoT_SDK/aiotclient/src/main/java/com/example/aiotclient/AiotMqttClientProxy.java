package com.example.aiotclient;

import android.app.Application;

import io.dcloud.weex.AppHookProxy;

public class AiotMqttClientProxy implements AppHookProxy {
    @Override
    public void onCreate(Application application) {
        // 可写初始化触发逻辑

    }
}
