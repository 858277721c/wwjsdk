package com.fanwe.lib.wwjsdk.sdk.callback;

import com.fanwe.lib.wwjsdk.sdk.response.WWCatchResultData;
import com.fanwe.lib.wwjsdk.sdk.response.WWCheckResultData;
import com.fanwe.lib.wwjsdk.sdk.response.WWHeartBeatData;

/**
 * 娃娃机sdk回调
 */
public interface WWControlSDKCallback
{
    /**
     * 接收到娃娃机抓取结果数据
     *
     * @param data
     */
    void onDataCatchResult(WWCatchResultData data);

    /**
     * 接收到检查娃娃机状态结果数据
     *
     * @param data
     */
    void onDataCheckResult(WWCheckResultData data);

    /**
     * 接收到娃娃机心跳数据
     *
     * @param data
     */
    void onDataHeartBeat(WWHeartBeatData data);
}
