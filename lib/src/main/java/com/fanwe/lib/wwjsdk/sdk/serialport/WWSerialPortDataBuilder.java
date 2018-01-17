package com.fanwe.lib.wwjsdk.sdk.serialport;

import com.fanwe.lib.wwjsdk.log.WWLogger;
import com.fanwe.lib.wwjsdk.sdk.request.WWInitParam;
import com.fanwe.lib.wwjsdk.utils.WWJsonUtil;

import java.util.logging.Level;

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

        if (param == null)
        {
            WWLogger.get().log(Level.INFO, "game init:");
        } else
        {
            WWLogger.get().log(Level.INFO, "game init:" + WWJsonUtil.objectToJson(param));
        }
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
