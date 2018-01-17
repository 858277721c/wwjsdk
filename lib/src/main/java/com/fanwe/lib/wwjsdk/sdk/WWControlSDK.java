package com.fanwe.lib.wwjsdk.sdk;

import com.fanwe.lib.wwjsdk.sdk.callback.WWControlSDKCallback;
import com.fanwe.lib.wwjsdk.sdk.serialport.IWWSerialPortDataBuilder;
import com.fanwe.lib.wwjsdk.sdk.serialport.WWSerialPort;
import com.fanwe.lib.wwjsdk.sdk.serialport.WWSerialPortDataBuilder;

/**
 * 娃娃机控制sdk
 */
public abstract class WWControlSDK implements IWWControlSDK
{
    private WWSerialPort mSerialPort;
    private IWWSerialPortDataBuilder mSerialDataBuilder;

    public WWControlSDK()
    {
        WWSDKManager.getInstance().checkInit();
        WWSDKManager.getInstance().setControlSDK(this);
        getSerialPort().open();
    }

    private WWSerialPort getSerialPort()
    {
        if (mSerialPort == null)
        {
            mSerialPort = provideSerialPort();
            if (mSerialPort == null)
            {
                throw new NullPointerException("you must provide a WWSerialPort before this");
            }
        }
        return mSerialPort;
    }

    private IWWSerialPortDataBuilder getSerialDataBuilder()
    {
        if (mSerialDataBuilder == null)
        {
            mSerialDataBuilder = provideSerialDataBuilder();
            if (mSerialDataBuilder == null)
            {
                throw new NullPointerException("you must provide a IWWSerialPortDataBuilder before this");
            }
        }
        return mSerialDataBuilder;
    }

    @Override
    public void init(String jsonInit)
    {
        getSerialDataBuilder().init(jsonInit);
    }

    @Override
    public final void setCallback(WWControlSDKCallback callback)
    {
        getSerialPort().setCallback(callback);
    }

    @Override
    public final boolean begin(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildBegin(jsonString);
        return sendData(data, "begin");
    }

    @Override
    public boolean moveFront(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildMove(jsonString, IWWSerialPortDataBuilder.Direction.Front);
        return sendData(data, "moveFront");
    }

    @Override
    public boolean moveBack(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildMove(jsonString, IWWSerialPortDataBuilder.Direction.Back);
        return sendData(data, "moveBack");
    }

    @Override
    public boolean moveLeft(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildMove(jsonString, IWWSerialPortDataBuilder.Direction.Left);
        return sendData(data, "moveLeft");
    }

    @Override
    public boolean moveRight(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildMove(jsonString, IWWSerialPortDataBuilder.Direction.Right);
        return sendData(data, "moveRight");
    }

    @Override
    public final boolean stopMove(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildStopMove(jsonString);
        return sendData(data, "stopMove");
    }

    @Override
    public final boolean doCatch(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildCatch(jsonString);
        return sendData(data, "doCatch");
    }

    @Override
    public final boolean check(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildCheck(jsonString);
        return sendData(data, "check");
    }

    @Override
    public final boolean sendData(byte[] data, String desc)
    {
        return getSerialPort().sendData(data, desc);
    }

    @Override
    public void onDestroy()
    {
        if (mSerialPort != null)
        {
            mSerialPort.onDestroy();
        }
    }

    /**
     * 提供一个将控制参数转为串口字节数据的对象
     *
     * @return
     */
    protected abstract WWSerialPortDataBuilder provideSerialDataBuilder();

    /**
     * 提供一个娃娃串口通信对象
     *
     * @return
     */
    protected abstract WWSerialPort provideSerialPort();
}
