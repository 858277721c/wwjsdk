package com.fanwe.lib.wwjsdk.xuebao;

import android.text.TextUtils;

import com.fanwe.lib.wwjsdk.sdk.IWWControlSDK;
import com.fanwe.lib.wwjsdk.sdk.WWSDKManager;
import com.fanwe.lib.wwjsdk.sdk.callback.WWControlSDKCallback;
import com.fanwe.lib.wwjsdk.sdk.proxy.IWWControlSDKProxy;
import com.fanwe.lib.wwjsdk.sdk.request.WWInitParam;
import com.fanwe.lib.wwjsdk.utils.FProbabilityHandler;
import com.fanwe.lib.wwjsdk.utils.WWJsonUtil;

/**
 * 娃娃机控制sdk，对外控制接口
 */
public final class WWControlSDKProxy implements IWWControlSDKProxy
{
    private String mJsonMove;
    private FProbabilityHandler mProbabilityHandler = new FProbabilityHandler();

    private String getJsonMove()
    {
        if (TextUtils.isEmpty(mJsonMove))
        {
            XueBaoWWMoveParam param = new XueBaoWWMoveParam();
            param.moveDuration = 5000;

            mJsonMove = WWJsonUtil.objectToJson(param);
        }
        return mJsonMove;
    }

    private IWWControlSDK getControlSDK()
    {
        return WWSDKManager.getInstance().getControlSDK();
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

        String jsonString = WWJsonUtil.objectToJson(param);
        getControlSDK().init(jsonString);
    }

    @Override
    public void init(int numerator, int denominator)
    {
        mProbabilityHandler.setNumerator(numerator);
        mProbabilityHandler.setDenominator(denominator);

        boolean keepCatch = mProbabilityHandler.random();
        init(keepCatch ? 1 : 0);
    }

    @Override
    public void setCallback(WWControlSDKCallback callback)
    {
        getControlSDK().setCallback(callback);
    }

    @Override
    public void begin()
    {
        getControlSDK().begin(null);
    }

    //---------- move start ----------

    @Override
    public void moveFront()
    {
        getControlSDK().moveBack(getJsonMove());
    }

    @Override
    public void moveBack()
    {
        getControlSDK().moveFront(getJsonMove());
    }

    @Override
    public void moveLeft()
    {
        getControlSDK().moveLeft(getJsonMove());
    }

    @Override
    public void moveRight()
    {
        getControlSDK().moveRight(getJsonMove());
    }

    //---------- move end ----------

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
