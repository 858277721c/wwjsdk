package com.fanwe.lib.wwjsdk.sdk.proxy;

import com.fanwe.lib.wwjsdk.sdk.IWWControlSDK;
import com.fanwe.lib.wwjsdk.sdk.WWSDKManager;
import com.fanwe.lib.wwjsdk.sdk.callback.WWControlSDKCallback;
import com.fanwe.lib.wwjsdk.sdk.request.WWInitParam;
import com.fanwe.lib.wwjsdk.utils.FProbabilityHandler;

/**
 * Created by Administrator on 2018/1/17.
 */
public abstract class WWControlSDKProxy implements IWWControlSDKProxy
{
    public WWControlSDKProxy()
    {

    }

    protected final IWWControlSDK getControlSDK()
    {
        return WWSDKManager.getInstance().getControlSDK();
    }

    @Override
    public void setCallback(WWControlSDKCallback callback)
    {
        getControlSDK().setCallback(callback);
    }

    @Override
    public void init(int keepCatch)
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
    public void init(int numerator, int denominator)
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
