package com.example.aiotclient;

import com.alibaba.fastjson.JSONObject;
import com.linkkit.aiotcore.AiotMqttClient;
import com.linkkit.aiotcore.AiotMqttException;
import com.linkkit.aiotcore.AiotMqttListener;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.utils.WXLogUtils;

/**
 * MQTT原生插件
 */
public class AiotMqttClientModule extends WXSDKEngine.DestroyableModule {

    AiotMqttClient mqttClient;

    /**
     * 初始化MQTT连接
     *
     * @param options
     * @param callback
     */
    @JSMethod
    public void connect(JSONObject options, final JSCallback callback) {

        String productKey = options.getString(IoTConfig.PRODUCT_KEY);
        if (productKey == null || productKey.isEmpty()) {
            invoke(false, "productKey不能为空", callback);
            return;
        }
        String deviceName = options.getString(IoTConfig.DEVICE_NAME);
        if (deviceName == null || deviceName.isEmpty()) {
            invoke(false, "deviceName不能为空", callback);
            return;
        }
        String deviceSecret = options.getString(IoTConfig.DEVICE_SECRET);
        if (deviceSecret == null || deviceSecret.isEmpty()) {
            invoke(false, "deviceSecret不能为空", callback);
            return;
        }
        try {
            mqttClient = new AiotMqttClient();
            mqttClient.setProductKey(productKey);
            mqttClient.setDeviceName(deviceName);
            mqttClient.setDeviceSecret(deviceSecret);
            mqttClient.connect();
            invoke(true, "初始化MQTT连接成功", callback);
        } catch (AiotMqttException e) {
            WXLogUtils.e(e.toString());
            invoke(false, "初始化MQTT连接异常：" + e.toString(), callback);
        }
    }

    /**
     * 发布MQTT消息
     *
     * @param options
     * @param callback
     */
    @JSMethod
    public void publish(JSONObject options, final JSCallback callback) {
        if (mqttClient != null) {
            try {
                String topic = options.getString(IoTConfig.TOPIC_NAME);
                byte[] payload = options.getBytes(IoTConfig.TOPIC_PAYLOAD);
                mqttClient.publish(topic, payload, 0);
                invoke(true, "发布MQTT消息成功", callback);
            } catch (AiotMqttException e) {
                invoke(false, "发布MQTT消息异常：" + e.getMessage(), callback);
                WXLogUtils.e(e.toString());
            }
        }
    }

    /**
     * 退订MQTT消息
     *
     * @param options
     * @param callback
     */
    @JSMethod
    public void unsubscribe(JSONObject options, final JSCallback callback) {
        if (mqttClient != null) {
            String topic = options.getString(IoTConfig.TOPIC_NAME);
            try {
                mqttClient.unsubscribe(topic);
                invoke(true, "退订MQTT消息成功", callback);
            } catch (AiotMqttException e) {
                WXLogUtils.e(e.toString());
                invoke(false, "退订MQTT消息异常：" + e.toString(), callback);
            }
        }
    }

    /**
     * 订阅MQTT消息
     *
     * @param options
     * @param subCallback
     * @param callback
     */
    @JSMethod
    public void subscribe(JSONObject options, final JSCallback subCallback, final JSCallback callback) {
        if (mqttClient != null) {
            String sub_topic = options.getString(IoTConfig.TOPIC_NAME);
            try {
                mqttClient.subscribe(sub_topic, 1, new AiotMqttListener() {
                    @Override
                    public void publishArrived(String topic, int qos, byte[] payload) {
                        SubscribeResult result = new SubscribeResult();
                        result.setTopic(topic);
                        result.setPayload(new String(payload));
                        invokeAndKeepAlive(true, result, subCallback);
                    }
                });
                invoke(true, "订阅MQTT消息成功", callback);
            } catch (AiotMqttException e) {
                WXLogUtils.e(e.toString());
                invoke(false, "订阅MQTT消息异常：" + e.toString(), callback);
            }
        }
    }

    /**
     * 断开MQTT连接
     *
     * @param callback
     */
    @JSMethod
    public void disconnect(final JSCallback callback) {
        try {
            this.disconnect();
            invoke(true, "断开MQTT连接成功", callback);
        } catch (AiotMqttException e) {
            WXLogUtils.e(e.toString());
            invoke(false, "断开MQTT连接失败：" + e.toString(), callback);
        }
    }

    @Override
    public void destroy() {
        try {
            this.disconnect();
        } catch (AiotMqttException e) {
            WXLogUtils.e(e.toString());
        }
    }

    /**
     * 断开MQTT连接
     */
    private void disconnect() throws AiotMqttException {
        if (mqttClient != null ) {
            mqttClient.disconnect();
        }
    }

    /**
     * js端应答
     *
     * @param success
     * @param data
     * @param callback
     */
    private void invoke(boolean success, Object data, final JSCallback callback) {
        JSONObject result = new JSONObject();
        result.put("success", success);
        result.put("data", data);
        callback.invoke(result);
    }

    /**
     * js端应答
     *
     * @param success
     * @param data
     * @param callback
     */
    private void invokeAndKeepAlive(boolean success, Object data, final JSCallback callback) {
        JSONObject result = new JSONObject();
        result.put("success", success);
        result.put("data", data);
        callback.invokeAndKeepAlive(result);
    }
}
