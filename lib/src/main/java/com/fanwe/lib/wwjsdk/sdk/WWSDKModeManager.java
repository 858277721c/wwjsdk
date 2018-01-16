package com.fanwe.lib.wwjsdk.sdk;

import com.fanwe.lib.http.PostRequest;
import com.fanwe.lib.http.callback.ModelRequestCallback;
import com.fanwe.lib.looper.FLooper;
import com.fanwe.lib.looper.impl.FSimpleLooper;
import com.fanwe.lib.wwjsdk.model.InitActModel;
import com.fanwe.lib.wwjsdk.model.WWServerConfig;
import com.fanwe.lib.wwjsdk.utils.WWJsonUtil;

/**
 * Created by Administrator on 2018/1/16.
 */
class WWSDKModeManager
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
        WWServerConfig config = WWServerConfig.get();
        if (config == null)
        {
            return;
        }
        String url = config.initUrl;
        PostRequest request = new PostRequest(url);
        request.param("key", "f8639bc67513dbbc3713ddc835b7f156");
        request.param("mac", "f8639bc67513dbbc3713ddc835b7f156");

        request.execute(new ModelRequestCallback<InitActModel>()
        {
            @Override
            public void onSuccess()
            {

            }

            @Override
            protected InitActModel parseToModel(String content, Class<InitActModel> clazz)
            {
                return WWJsonUtil.jsonToObject(content, clazz);
            }
        });
    }
}
