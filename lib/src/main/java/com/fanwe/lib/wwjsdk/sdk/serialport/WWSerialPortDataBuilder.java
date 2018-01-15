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
    public void init(String jsonInit)
    {
        try
        {
            mInitParam = null;
            mInitParam = WWJsonUtil.jsonToObject(jsonInit, WWInitParam.class);
            WWLogger.get().log(Level.INFO, "parse jsonInit:" + jsonInit);
        } catch (Exception e)
        {
            WWLogger.get().log(Level.SEVERE, "parse jsonInit error:" + e + " " + jsonInit, e);
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

    @Override
    public final byte[] buildBegin(String jsonString)
    {
        return onBuildBegin(jsonString);
    }

    @Override
    public final byte[] buildMove(String jsonString, Direction direction)
    {
        return onBuildMove(jsonString, direction);
    }

    @Override
    public final byte[] buildStopMove(String jsonString)
    {
        return onBuildStopMove(jsonString);
    }

    @Override
    public final byte[] buildCatch(String jsonString)
    {
        return onBuildCatch(jsonString);
    }

    @Override
    public final byte[] buildCheck(String jsonString)
    {
        return onBuildCheck(jsonString);
    }

    protected abstract byte[] onBuildBegin(String jsonString);

    protected abstract byte[] onBuildMove(String jsonString, Direction direction);

    protected abstract byte[] onBuildStopMove(String jsonString);

    protected abstract byte[] onBuildCatch(String jsonString);

    protected abstract byte[] onBuildCheck(String jsonString);
}
