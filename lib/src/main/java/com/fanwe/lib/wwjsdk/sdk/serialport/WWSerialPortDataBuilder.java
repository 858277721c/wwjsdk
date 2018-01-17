package com.fanwe.lib.wwjsdk.sdk.serialport;

import com.fanwe.lib.wwjsdk.sdk.request.WWInitParam;

/**
 * 将控制参数转为串口字节数据
 */
public abstract class WWSerialPortDataBuilder implements IWWSerialPortDataBuilder
{
    private WWInitParam mInitParam;

    @Override
    public void init(WWInitParam param)
    {
        mInitParam = param;
    }

    /**
     * 返回本局游戏初始化设置的控制参数对象
     *
     * @return
     */
    public final WWInitParam getInitParam()
    {
        if (mInitParam == null)
        {
            mInitParam = new WWInitParam();
        }
        return mInitParam;
    }
}
