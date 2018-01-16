package com.fanwe.lib.wwjsdk.common;

import com.fanwe.lib.looper.FLooper;
import com.fanwe.lib.looper.impl.FSimpleLooper;

/**
 * Created by Administrator on 2018/1/16.
 */
public class WWSDKModeManager
{
    private static WWSDKModeManager sInstance;

    private FLooper mLooper = new FSimpleLooper();

    private WWSDKModeManager()
    {
    }

    public static WWSDKModeManager getInstance()
    {
        if (sInstance == null)
        {
            synchronized (WWSDKModeManager.class)
            {
                if (sInstance == null)
                {
                    sInstance = new WWSDKModeManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 开始监听
     *
     * @param start
     */
    public void startMonitor(boolean start)
    {
        if (start)
        {
            mLooper.start(30 * 1000, new Runnable()
            {
                @Override
                public void run()
                {
                    onMonitor();
                }
            });
        } else
        {
            mLooper.stop();
        }
    }

    private void onMonitor()
    {

    }
}
