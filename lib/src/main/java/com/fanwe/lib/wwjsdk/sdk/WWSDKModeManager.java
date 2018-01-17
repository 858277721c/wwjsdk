package com.fanwe.lib.wwjsdk.sdk;

import android.text.TextUtils;

import com.fanwe.lib.http.PostRequest;
import com.fanwe.lib.http.callback.ModelRequestCallback;
import com.fanwe.lib.looper.FLooper;
import com.fanwe.lib.looper.impl.FSimpleLooper;
import com.fanwe.lib.wwjsdk.log.WWLogger;
import com.fanwe.lib.wwjsdk.model.InitActModel;
import com.fanwe.lib.wwjsdk.model.WWServerConfig;
import com.fanwe.lib.wwjsdk.socketio.WWSocket;
import com.fanwe.lib.wwjsdk.utils.WWJsonUtil;

import java.util.logging.Level;

/**
 * Created by Administrator on 2018/1/16.
 */
class WWSDKModeManager
{
    private static WWSDKModeManager sInstance;

    private FLooper mLooper = new FSimpleLooper();
    private WWSocket mSocket = new WWSocket();
    private int mMode = Mode.TENCENT;

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
     * 返回当前的sdk模式
     *
     * @return {@link Mode}
     */
    public int getMode()
    {
        return mMode;
    }

    /**
     * 开始监听
     */
    public void startMonitor()
    {
        mLooper.start(30 * 1000, new Runnable()
        {
            @Override
            public void run()
            {
                WWLogger.get().log(Level.INFO, "monitor sdk mode...");
                onMonitor();
            }
        });
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

        request.execute(new ModelRequestCallback<InitActModel>()
        {
            @Override
            public void onSuccess()
            {
                WWLogger.get().log(Level.INFO, "request init result:" + getResult());
                if (getActModel().getStatus() == 1)
                {
                    dealRequestResult(getActModel());
                }
            }

            @Override
            public void onError(Exception e)
            {
                super.onError(e);
                WWLogger.get().log(Level.SEVERE, "request init error:" + e, e);
            }

            @Override
            protected InitActModel parseToModel(String content, Class<InitActModel> clazz)
            {
                return WWJsonUtil.jsonToObject(content, clazz);
            }
        });
    }

    private void dealRequestResult(InitActModel model)
    {
        mMode = model.getType();

        if (mMode == Mode.FANWE)
        {
            String url = model.getSocket_address();
            if (!TextUtils.isEmpty(url))
            {
                if (!url.equals(mSocket.getUrl()))
                {
                    mSocket.disconnect();
                    mSocket.setUrl(url);
                }
                mSocket.connect();
            } else
            {
                WWLogger.get().log(Level.SEVERE, "init param error:empty socket_address");
            }
        } else
        {
            mSocket.disconnect();
        }
    }

    public static final class Mode
    {
        public static final int TENCENT = 0;
        public static final int FANWE = 1;
    }
}
