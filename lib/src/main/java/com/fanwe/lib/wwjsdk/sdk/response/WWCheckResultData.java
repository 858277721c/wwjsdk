package com.fanwe.lib.wwjsdk.sdk.response;

import com.fanwe.lib.wwjsdk.sdk.constants.WWState;

/**
 * 检查娃娃机状态结果数据
 */
public class WWCheckResultData extends WWResponseData
{
    /**
     * 娃娃机状态{@link WWState}
     */
    public int state = WWState.ERROR;
    /**
     * 状态描述
     */
    public String stateDesc;
}
