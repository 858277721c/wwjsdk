package com.fanwe.lib.wwjsdk.sdk.request;

/**
 * 娃娃机控制参数
 */
public class WWControlParam
{
    /**
     * 直接控制娃娃机的原始数据，如果不为空，则采用原始数据透传
     */
    public byte[] dataOriginal;

    /**
     * 是否有直接控制娃娃机的原始数据
     *
     * @return
     */
    public boolean hasDataOriginal()
    {
        return dataOriginal != null && dataOriginal.length > 0;
    }
}
