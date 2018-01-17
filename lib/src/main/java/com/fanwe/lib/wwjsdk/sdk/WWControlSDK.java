package com.fanwe.lib.wwjsdk.sdk;

import android.content.Context;
import android.text.TextUtils;

import com.fanwe.lib.wwjsdk.R;
import com.fanwe.lib.wwjsdk.WWSDKManager;
import com.fanwe.lib.wwjsdk.log.WWLogger;
import com.fanwe.lib.wwjsdk.sdk.callback.WWControlSDKCallback;
import com.fanwe.lib.wwjsdk.sdk.request.WWInitParam;
import com.fanwe.lib.wwjsdk.sdk.response.WWCatchResultData;
import com.fanwe.lib.wwjsdk.sdk.response.WWCheckResultData;
import com.fanwe.lib.wwjsdk.sdk.response.WWHeartBeatData;
import com.fanwe.lib.wwjsdk.sdk.serialport.IWWSerialPortDataBuilder;
import com.fanwe.lib.wwjsdk.sdk.serialport.WWSerialPort;
import com.fanwe.lib.wwjsdk.sdk.serialport.WWSerialPortDataBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * 娃娃机控制sdk
 */
public abstract class WWControlSDK implements IWWControlSDK
{
    private static IWWControlSDK sInstance;

    private WWSerialPort mSerialPort;
    private IWWSerialPortDataBuilder mSerialDataBuilder;

    private List<WWControlSDKCallback> mListCallback = new ArrayList<>();

    protected WWControlSDK()
    {
        WWSDKManager.getInstance().checkInit();
        getSerialPort().open();
    }

    public static final IWWControlSDK getInstance()
    {
        if (sInstance == null)
        {
            synchronized (WWControlSDK.class)
            {
                if (sInstance == null)
                {
                    sInstance = newInstance();
                }
            }
        }
        return sInstance;
    }

    private final static WWControlSDK newInstance()
    {
        final Context context = WWSDKManager.getInstance().getContext();
        final String className = context.getResources().getString(R.string.class_ww_control_sdk);
        if (!TextUtils.isEmpty(className))
        {
            final String prefix = "create sdk (" + className + ")";
            WWLogger.get().log(Level.INFO, "try " + prefix);
            try
            {
                Class clazz = Class.forName(className);
                Object object = clazz.newInstance();
                if (object instanceof WWControlSDK)
                {
                    return (WWControlSDK) object;
                } else
                {
                    throw new RuntimeException("\"class_ww_control_sdk\" value in your string.xml must be instance of com.fanwe.lib.wwjsdk.sdk.WWControlSDK");
                }
            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        } else
        {
            throw new RuntimeException("\"class_ww_control_sdk\" is not specify in your string.xml for example:" + "\r\n" +
                    "<string name=\"class_ww_control_sdk\">com.fanwe.lib.wwjsdk.xuebao.XueBaoWWControlSDK</string>");
        }
    }

    private final WWSerialPort getSerialPort()
    {
        if (mSerialPort == null)
        {
            mSerialPort = provideSerialPort();
            if (mSerialPort == null)
            {
                throw new NullPointerException("you must provide a WWSerialPort before this");
            }
            mSerialPort.setCallback(new WWControlSDKCallback()
            {
                @Override
                public void onDataCatchResult(WWCatchResultData data)
                {
                    synchronized (WWControlSDK.this)
                    {
                        for (WWControlSDKCallback item : mListCallback)
                        {
                            item.onDataCatchResult(data);
                        }
                    }
                }

                @Override
                public void onDataCheckResult(WWCheckResultData data)
                {
                    synchronized (WWControlSDK.this)
                    {
                        for (WWControlSDKCallback item : mListCallback)
                        {
                            item.onDataCheckResult(data);
                        }
                    }
                }

                @Override
                public void onDataHeartBeat(WWHeartBeatData data)
                {
                    synchronized (WWControlSDK.this)
                    {
                        for (WWControlSDKCallback item : mListCallback)
                        {
                            item.onDataHeartBeat(data);
                        }
                    }
                }
            });
        }
        return mSerialPort;
    }

    private final IWWSerialPortDataBuilder getSerialDataBuilder()
    {
        if (mSerialDataBuilder == null)
        {
            mSerialDataBuilder = provideSerialDataBuilder();
            if (mSerialDataBuilder == null)
            {
                throw new NullPointerException("you must provide a WWSerialPortDataBuilder before this");
            }
        }
        return mSerialDataBuilder;
    }

    @Override
    public final synchronized void addCallback(WWControlSDKCallback callback)
    {
        if (callback == null || mListCallback.contains(callback))
        {
            return;
        }
        if (mListCallback.add(callback))
        {
            WWLogger.get().log(Level.INFO, "add sdk callback:" + callback + " size:" + mListCallback.size());
        }
    }

    @Override
    public final synchronized void removeCallback(WWControlSDKCallback callback)
    {
        if (mListCallback.remove(callback))
        {
            WWLogger.get().log(Level.INFO, "remove sdk callback:" + callback + " size:" + mListCallback.size());
        }
    }

    @Override
    public final void init(WWInitParam param)
    {
        getSerialDataBuilder().init(param);
    }

    @Override
    public final boolean begin(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildBegin(jsonString);
        return sendData(data, "begin");
    }

    @Override
    public final boolean moveFront(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildMove(jsonString, IWWSerialPortDataBuilder.Direction.Front);
        return sendData(data, "moveFront");
    }

    @Override
    public final boolean moveBack(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildMove(jsonString, IWWSerialPortDataBuilder.Direction.Back);
        return sendData(data, "moveBack");
    }

    @Override
    public final boolean moveLeft(String jsonString)
    {
        byte[] data = getSerialDataBuilder().buildMove(jsonString, IWWSerialPortDataBuilder.Direction.Left);
        return sendData(data, "moveLeft");
    }

    @Override
    public final boolean moveRight(String jsonString)
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
            mSerialPort.close();
            mSerialPort.setCallback(null);
            mSerialPort = null;
            sInstance = null;
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
