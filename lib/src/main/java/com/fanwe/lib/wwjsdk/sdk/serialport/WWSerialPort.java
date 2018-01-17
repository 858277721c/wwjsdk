package com.fanwe.lib.wwjsdk.sdk.serialport;

import com.fanwe.lib.serialport.FISerialPort;
import com.fanwe.lib.serialport.FInputStreamReadThread;
import com.fanwe.lib.serialport.FSerialPort;
import com.fanwe.lib.serialport.FSimpleSerialPort;
import com.fanwe.lib.wwjsdk.log.WWLogger;
import com.fanwe.lib.wwjsdk.sdk.callback.WWControlSDKCallback;
import com.fanwe.lib.wwjsdk.sdk.response.WWCatchResultData;
import com.fanwe.lib.wwjsdk.sdk.response.WWCheckResultData;
import com.fanwe.lib.wwjsdk.sdk.response.WWHeartBeatData;
import com.fanwe.lib.wwjsdk.utils.WWUtils;

import java.io.IOException;
import java.util.logging.Level;

/**
 * 娃娃机串口通信
 */
public abstract class WWSerialPort
{
    private FSimpleSerialPort mSerialPort;
    private WWControlSDKCallback mCallback;

    private FSerialPort getSerialPort()
    {
        if (mSerialPort == null)
        {
            mSerialPort = new FSimpleSerialPort()
            {
                @Override
                protected FInputStreamReadThread.ReadConfig provideReadConfig()
                {
                    return WWSerialPort.this.provideReadConfig();
                }

                @Override
                protected void onReadData(byte[] data, int readSize)
                {
                    WWSerialPort.this.onReadData(data, readSize);
                }

                @Override
                protected void onReadError(Exception e)
                {
                    WWLogger.get().log(Level.SEVERE, "SerialPort read error:" + e, e);
                }
            };
        }
        return mSerialPort;
    }

    public final void setCallback(WWControlSDKCallback callback)
    {
        mCallback = callback;
    }

    protected final WWControlSDKCallback getCallback()
    {
        if (mCallback == null)
        {
            mCallback = new WWControlSDKCallback()
            {
                @Override
                public void onDataCatchResult(WWCatchResultData data)
                {
                }

                @Override
                public void onDataCheckResult(WWCheckResultData data)
                {
                }

                @Override
                public void onDataHeartBeat(WWHeartBeatData data)
                {
                }
            };
        }
        return mCallback;
    }

    /**
     * 初始化串口
     *
     * @param path     串口路径
     * @param baudRate 波特率
     */
    public final void init(String path, int baudRate)
    {
        FISerialPort.Config config = new FISerialPort.Config();
        config.path = path;
        config.baudrate = baudRate;

        getSerialPort().setConfig(config);
    }

    /**
     * 打开串口
     *
     * @return true-串口已经打开
     */
    public final boolean open()
    {
        if (getSerialPort().isOpened())
        {
            return true;
        }
        try
        {
            getSerialPort().open();
            WWLogger.get().log(Level.INFO, "SerialPort open:" + getSerialPort().getConfig().path + " " +
                    getSerialPort().getConfig().baudrate);
            return true;
        } catch (Exception e)
        {
            WWLogger.get().log(Level.SEVERE, "SerialPort open error:" + e, e);
            return false;
        }
    }

    /**
     * 发送数据
     *
     * @param data
     * @param desc
     * @return
     */
    public boolean sendData(byte[] data, String desc)
    {
        final String suffix = WWUtils.byte2HexString(data, data.length) + " " + desc;
        if (!getSerialPort().isOpened())
        {
            WWLogger.get().log(Level.WARNING, "send data error: SerialPort is not open " + suffix);
            return false;
        }
        try
        {
            getSerialPort().getOutputStream().write(data);
            getSerialPort().getOutputStream().flush();
            WWLogger.get().log(Level.INFO, "send data " + suffix);
            return true;
        } catch (IOException e)
        {
            WWLogger.get().log(Level.SEVERE, " send data error: " + e + " " + suffix, e);
            return false;
        }
    }

    /**
     * 返回一个读取配置
     *
     * @return
     */
    protected abstract FInputStreamReadThread.ReadConfig provideReadConfig();

    /**
     * 读取到数据
     *
     * @param data
     * @param readSize
     */
    protected abstract void onReadData(byte[] data, int readSize);

    /**
     * 关闭串口
     */
    public void close()
    {
        if (mSerialPort != null)
        {
            mSerialPort.close();
            mSerialPort = null;
            WWLogger.get().log(Level.INFO, "SerialPort close");
        }
    }
}
