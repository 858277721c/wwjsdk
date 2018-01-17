package com.fanwe.lib.wwjsdk;

import android.text.TextUtils;

import com.fanwe.lib.http.PostRequest;
import com.fanwe.lib.http.callback.ModelRequestCallback;
import com.fanwe.lib.looper.FLooper;
import com.fanwe.lib.looper.impl.FSimpleLooper;
import com.fanwe.lib.wwjsdk.log.WWLogger;
import com.fanwe.lib.wwjsdk.model.InitActModel;
import com.fanwe.lib.wwjsdk.model.WWServerConfig;
import com.fanwe.lib.wwjsdk.sdk.IWWControlSDK;
import com.fanwe.lib.wwjsdk.sdk.WWControlSDK;
import com.fanwe.lib.wwjsdk.socketio.WWSocket;
import com.fanwe.lib.wwjsdk.utils.WWJsonUtil;

import java.util.logging.Level;

/**
 * Created by Administrator on 2018/1/16.
 */
public class WWSDKModeManager
{
    private static WWSDKModeManager sInstance;

    private FLooper mLooper = new FSimpleLooper();
    private WWSocket mSocket = new WWSocket();
    private int mMode = Mode.OTHER;

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
     * 根据请求的mode返回sdk对象
     *
     * @param requestMode {@link Mode}
     * @return
     */
    public IWWControlSDK getControlSDKByMode(int requestMode)
    {
        if (mMode == requestMode)
        {
            return WWControlSDK.getInstance();
        } else
        {
            return IWWControlSDK.EMPTY;
        }
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
                WWLogger.get().log(Level.INFO, "current mode:" + mMode + " start check sdk mode...");
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
                if (getActModel() == null)
                {
                    WWLogger.get().log(Level.SEVERE, "request init error: actModel is null");
                    return;
                }

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
        public static final int OTHER = 0;
        public static final int FANWE = 1;
    }
}
