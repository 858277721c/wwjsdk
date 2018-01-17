package com.fanwe.lib.wwjsdk.sdk.proxy;

import android.content.Context;
import android.text.TextUtils;

import com.fanwe.lib.wwjsdk.R;
import com.fanwe.lib.wwjsdk.WWSDKManager;
import com.fanwe.lib.wwjsdk.WWSDKModeManager;
import com.fanwe.lib.wwjsdk.log.WWLogger;
import com.fanwe.lib.wwjsdk.sdk.IWWControlSDK;
import com.fanwe.lib.wwjsdk.sdk.WWControlSDK;
import com.fanwe.lib.wwjsdk.sdk.callback.WWControlSDKCallback;
import com.fanwe.lib.wwjsdk.sdk.request.WWInitParam;
import com.fanwe.lib.wwjsdk.utils.FProbabilityHandler;

import java.util.logging.Level;

/**
 * Created by Administrator on 2018/1/17.
 */
public abstract class WWControlSDKProxy implements IWWControlSDKProxy
{
    private static IWWControlSDKProxy sInstance;

    protected WWControlSDKProxy()
    {
    }

    public static IWWControlSDKProxy getInstance()
    {
        if (sInstance == null)
        {
            synchronized (WWControlSDKProxy.class)
            {
                if (sInstance == null)
                {
                    sInstance = newInstance();
                }
            }
        }
        return sInstance;
    }

    private static WWControlSDKProxy newInstance()
    {
        final Context context = WWSDKManager.getInstance().getContext();
        final String className = context.getResources().getString(R.string.class_ww_control_sdk_proxy);
        if (!TextUtils.isEmpty(className))
        {
            final String prefix = "create sdk proxy (" + className + ")";
            WWLogger.get().log(Level.INFO, "try " + prefix);
            try
            {
                Class clazz = Class.forName(className);
                Object object = clazz.newInstance();
                if (object instanceof WWControlSDKProxy)
                {
                    return (WWControlSDKProxy) object;
                } else
                {
                    throw new RuntimeException("\"class_ww_control_sdk_proxy\" value in your string.xml must be instance of com.fanwe.lib.wwjsdk.sdk.proxy.WWControlSDKProxy");
                }
            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        } else
        {
            throw new RuntimeException("\"class_ww_control_sdk_proxy\" is not specify in your string.xml for example:" + "\r\n" +
                    "<string name=\"class_ww_control_sdk_proxy\">com.fanwe.lib.wwjsdk.xuebao.XueBaoWWControlSDKProxy</string>");
        }
    }

    protected final IWWControlSDK getControlSDK()
    {
        return WWControlSDK.getInstanceByMode(WWSDKModeManager.Mode.OTHER);
    }

    @Override
    public final void addCallback(WWControlSDKCallback callback)
    {
        WWControlSDK.getInstance().addCallback(callback);
    }

    @Override
    public final void removeCallback(WWControlSDKCallback callback)
    {
        WWControlSDK.getInstance().removeCallback(callback);
    }

    @Override
    public final void init(int keepCatch)
    {
        WWInitParam param = new WWInitParam();
        if (keepCatch == 1)
        {
            param.keepCatch = 1;
        } else
        {
            param.keepCatch = 0;
        }

        getControlSDK().init(param);
    }

    @Override
    public final void init(int numerator, int denominator)
    {
        FProbabilityHandler probabilityHandler = new FProbabilityHandler();
        probabilityHandler.setNumerator(numerator);
        probabilityHandler.setDenominator(denominator);

        boolean keepCatch = probabilityHandler.random();
        init(keepCatch ? 1 : 0);
    }

    @Override
    public void begin()
    {
        getControlSDK().begin(null);
    }

    @Override
    public void moveFront()
    {
        getControlSDK().moveBack(null);
    }

    @Override
    public void moveBack()
    {
        getControlSDK().moveFront(null);
    }

    @Override
    public void moveLeft()
    {
        getControlSDK().moveLeft(null);
    }

    @Override
    public void moveRight()
    {
        getControlSDK().moveRight(null);
    }

    @Override
    public void stopMove()
    {
        getControlSDK().stopMove(null);
    }

    @Override
    public void doCatch()
    {
        getControlSDK().doCatch(null);
    }

    @Override
    public void check()
    {
        getControlSDK().check(null);
    }
}
