package com.fanwe.lib.wwjsdk.socketio;

import android.util.Log;

import com.fanwe.lib.wwjsdk.log.WWLogger;
import com.fanwe.lib.wwjsdk.sdk.IWWControlSDK;
import com.fanwe.lib.wwjsdk.sdk.callback.WWControlSDKCallback;
import com.fanwe.lib.wwjsdk.sdk.request.WWControlParam;
import com.fanwe.lib.wwjsdk.sdk.response.WWCatchResultData;
import com.fanwe.lib.wwjsdk.sdk.response.WWCheckResultData;
import com.fanwe.lib.wwjsdk.sdk.response.WWHeartBeatData;
import com.fanwe.lib.wwjsdk.socketio.listener.SocketJsonListener;
import com.fanwe.lib.wwjsdk.utils.WWJsonUtil;

import java.net.URISyntaxException;
import java.util.logging.Level;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Administrator on 2017/12/19.
 */
public class WWSocket implements WWControlSDKCallback
{
    public static final String EVENT_INIT = "ww_init";
    public static final String EVENT_BEGIN = "ww_begin";

    public static final String EVENT_FRONT = "ww_front";
    public static final String EVENT_BACK = "ww_back";
    public static final String EVENT_LEFT = "ww_left";
    public static final String EVENT_RIGHT = "ww_right";
    public static final String EVENT_STOP_MOVE = "ww_stop_move";

    public static final String EVENT_CATCH = "ww_catch";
    public static final String EVENT_CHECK = "ww_check";

    public static final String EVENT_RESPONSE_CHECK = "ww_response_check";
    public static final String EVENT_RESPONSE_CATCH = "ww_response_catch";
    public static final String EVENT_RESPONSE_HEART_BEAT = "ww_response_heart_beat";

    private Socket mSocket;
    private IWWControlSDK mControlSDK;

    public void setControlSDK(IWWControlSDK controlSDK)
    {
        mControlSDK = controlSDK;
    }

    public boolean isConnected()
    {
        return mSocket != null && mSocket.connected();
    }

    /**
     * 初始化socket
     *
     * @param url
     */
    public void initSocket(String url)
    {
        if (isConnected())
        {
            return;
        }

        IO.Options options = new IO.Options();
        options.reconnectionDelay = 100;
        try
        {
            mSocket = IO.socket(url, options);
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener()
            {
                @Override
                public void call(Object... args)
                {
                    WWLogger.get().log(Level.INFO, "Socket connect");
                }
            });
            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener()
            {
                @Override
                public void call(Object... args)
                {
                    WWLogger.get().log(Level.INFO, "Socket disconnect");
                }
            });

            //---------- WWControlParam start ----------
            mSocket.on(EVENT_INIT, new SocketJsonListener(EVENT_INIT)
            {
                @Override
                protected void onReceive(String json, Object... args)
                {
                    mControlSDK.init(json);
                    sendData(getEvent(), SocketResponseModel.newOk(null));
                }
            });
            mSocket.on(EVENT_BEGIN, new WWControlParamListener(EVENT_BEGIN)
            {
                @Override
                protected void onReceive(WWControlParam param, String json, Object... args)
                {
                    sendControlResultData(getEvent(), mControlSDK.begin(json));
                }
            });
            mSocket.on(EVENT_FRONT, new WWControlParamListener(EVENT_FRONT)
            {
                @Override
                protected void onReceive(WWControlParam param, String json, Object[] args)
                {
                    sendControlResultData(getEvent(), mControlSDK.moveFront(json));
                }
            });
            mSocket.on(EVENT_BACK, new WWControlParamListener(EVENT_BACK)
            {
                @Override
                protected void onReceive(WWControlParam param, String json, Object[] args)
                {
                    sendControlResultData(getEvent(), mControlSDK.moveBack(json));
                }
            });
            mSocket.on(EVENT_LEFT, new WWControlParamListener(EVENT_LEFT)
            {
                @Override
                protected void onReceive(WWControlParam param, String json, Object[] args)
                {
                    sendControlResultData(getEvent(), mControlSDK.moveLeft(json));
                }
            });
            mSocket.on(EVENT_RIGHT, new WWControlParamListener(EVENT_RIGHT)
            {
                @Override
                protected void onReceive(WWControlParam param, String json, Object[] args)
                {
                    sendControlResultData(getEvent(), mControlSDK.moveRight(json));
                }
            });
            mSocket.on(EVENT_STOP_MOVE, new WWControlParamListener(EVENT_STOP_MOVE)
            {
                @Override
                protected void onReceive(WWControlParam param, String json, Object[] args)
                {
                    sendControlResultData(getEvent(), mControlSDK.stopMove(json));
                }
            });
            mSocket.on(EVENT_CATCH, new WWControlParamListener(EVENT_CATCH)
            {
                @Override
                protected void onReceive(WWControlParam param, String json, Object[] args)
                {
                    sendControlResultData(getEvent(), mControlSDK.doCatch(json));
                }
            });
            mSocket.on(EVENT_CHECK, new WWControlParamListener(EVENT_CHECK)
            {
                @Override
                protected void onReceive(WWControlParam param, String json, Object[] args)
                {
                    sendControlResultData(getEvent(), mControlSDK.check(json));
                }
            });

            //---------- WWControlParam end ----------

            mSocket.connect();
            WWLogger.get().log(Level.INFO, "Socket try connect:" + url);
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
            WWLogger.get().log(Level.WARNING, "Socket connect error: " + e, e);
        }
    }

    private void sendControlResultData(String event, boolean result)
    {
        if (result)
        {
            sendData(event, SocketResponseModel.newOk(null));
        } else
        {
            sendData(event, SocketResponseModel.newInternalError(null));
        }
    }

    private void sendData(String event, Object data)
    {
        if (mSocket.connected())
        {
            String resultData = null;
            if (data instanceof String)
            {
                resultData = (String) data;
            } else
            {
                resultData = WWJsonUtil.objectToJson(data);
            }

            Log.i(WWSocket.class.getSimpleName(), "sendData (" + event + ") " + resultData);
            mSocket.emit(event, resultData);
        }
    }

    @Override
    public void onDataCatchResult(WWCatchResultData data)
    {
        sendData(EVENT_RESPONSE_CATCH, data);
    }

    @Override
    public void onDataCheckResult(WWCheckResultData data)
    {
        sendData(EVENT_RESPONSE_CHECK, data);
    }

    @Override
    public void onDataHeartBeat(WWHeartBeatData data)
    {
        sendData(EVENT_RESPONSE_HEART_BEAT, data);
    }

    public void onDestroy()
    {
        if (mSocket != null)
        {
            mSocket.disconnect();
            WWLogger.get().log(Level.INFO, "Socket onDestroy");
        }
    }

    private abstract class WWControlParamListener extends SocketJsonListener
    {
        public WWControlParamListener(String event)
        {
            super(event);
        }

        @Override
        protected final void onReceive(String json, Object... args)
        {
            WWControlParam param = WWJsonUtil.jsonToObject(json, WWControlParam.class);
            if (param != null && param.hasDataOriginal())
            {
                // 如果有原始数据，则只做透传
                boolean result = mControlSDK.sendData(param.dataOriginal, getEvent());
                sendControlResultData(getEvent(), result);
            } else
            {
                onReceive(param, json, args);
            }
        }

        protected abstract void onReceive(WWControlParam param, String json, Object... args);
    }
}
