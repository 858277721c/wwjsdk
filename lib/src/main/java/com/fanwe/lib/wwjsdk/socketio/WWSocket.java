package com.fanwe.lib.wwjsdk.socketio;

import android.text.TextUtils;
import android.util.Log;

import com.fanwe.lib.wwjsdk.WWSDKModeManager;
import com.fanwe.lib.wwjsdk.log.WWLogger;
import com.fanwe.lib.wwjsdk.sdk.IWWControlSDK;
import com.fanwe.lib.wwjsdk.sdk.WWControlSDK;
import com.fanwe.lib.wwjsdk.sdk.callback.WWControlSDKCallback;
import com.fanwe.lib.wwjsdk.sdk.request.WWControlParam;
import com.fanwe.lib.wwjsdk.sdk.request.WWInitParam;
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
public class WWSocket
{
    /**
     * 透传
     */
    private static final String EVENT_SEND_DATA = "ww_send_data";
    /**
     * 初始化
     */
    private static final String EVENT_INIT = "ww_init";
    /**
     * 开始游戏
     */
    private static final String EVENT_BEGIN = "ww_begin";
    /**
     * 向前移动爪子
     */
    private static final String EVENT_FRONT = "ww_front";
    /**
     * 向后移动爪子
     */
    private static final String EVENT_BACK = "ww_back";
    /**
     * 向左移动爪子
     */
    private static final String EVENT_LEFT = "ww_left";
    /**
     * 向右移动爪子
     */
    private static final String EVENT_RIGHT = "ww_right";
    /**
     * 停止移动爪子
     */
    private static final String EVENT_STOP_MOVE = "ww_stop_move";
    /**
     * 下爪
     */
    private static final String EVENT_CATCH = "ww_catch";
    /**
     * 检测娃娃机状态
     */
    private static final String EVENT_CHECK = "ww_check";

    /**
     * 娃娃机返回检测结果
     */
    private static final String EVENT_RESPONSE_CHECK = "ww_response_check";
    /**
     * 娃娃机返回抓取结果
     */
    private static final String EVENT_RESPONSE_CATCH = "ww_response_catch";
    /**
     * 娃娃机返回心跳
     */
    private static final String EVENT_RESPONSE_HEART_BEAT = "ww_response_heart_beat";

    private Socket mSocket;
    private String mUrl;

    private IWWControlSDK getControlSDK()
    {
        return WWControlSDK.getInstanceByMode(WWSDKModeManager.Mode.FANWE);
    }

    /**
     * 连接socket服务器
     *
     * @param url
     */
    public void connect(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return;
        }
        if (!url.equals(mUrl))
        {
            disconnect();
            mUrl = url;
        }
        connect();
    }

    public boolean isConnected()
    {
        return mSocket != null && mSocket.connected();
    }

    private void connect()
    {
        if (isConnected())
        {
            return;
        }
        disconnect();

        final String url = mUrl;
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
                    WWLogger.get().log(Level.INFO, "Socket connected");
                    WWControlSDK.getInstance().addCallback(mControlSDKCallback);
                }
            });
            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener()
            {
                @Override
                public void call(Object... args)
                {
                    WWLogger.get().log(Level.INFO, "Socket disconnected");
                }
            });

            //---------- WWControlParam start ----------
            mSocket.on(EVENT_SEND_DATA, new SocketJsonListener(EVENT_SEND_DATA)
            {
                @Override
                protected void onReceive(String json, Object... args)
                {
                    WWControlParam param = WWJsonUtil.jsonToObject(json, WWControlParam.class);
                    if (param != null && param.hasDataOriginal())
                    {
                        // 透传
                        boolean result = getControlSDK().sendData(param.dataOriginal, getEvent());
                        sendControlResultData(getEvent(), result);
                    } else
                    {
                        sendData(getEvent(), SocketResponseModel.newErrorParam("dataOriginal not found"));
                    }
                }
            });
            mSocket.on(EVENT_INIT, new SocketJsonListener(EVENT_INIT)
            {
                @Override
                protected void onReceive(String json, Object... args)
                {
                    WWInitParam initParam = WWJsonUtil.jsonToObject(json, WWInitParam.class);

                    getControlSDK().init(initParam);
                    sendData(getEvent(), SocketResponseModel.newOk(null));
                }
            });
            mSocket.on(EVENT_BEGIN, new SocketJsonListener(EVENT_BEGIN)
            {
                @Override
                protected void onReceive(String json, Object... args)
                {
                    sendControlResultData(getEvent(), getControlSDK().begin(json));
                }
            });
            mSocket.on(EVENT_FRONT, new SocketJsonListener(EVENT_FRONT)
            {
                @Override
                protected void onReceive(String json, Object[] args)
                {
                    sendControlResultData(getEvent(), getControlSDK().moveFront(json));
                }
            });
            mSocket.on(EVENT_BACK, new SocketJsonListener(EVENT_BACK)
            {
                @Override
                protected void onReceive(String json, Object[] args)
                {
                    sendControlResultData(getEvent(), getControlSDK().moveBack(json));
                }
            });
            mSocket.on(EVENT_LEFT, new SocketJsonListener(EVENT_LEFT)
            {
                @Override
                protected void onReceive(String json, Object[] args)
                {
                    sendControlResultData(getEvent(), getControlSDK().moveLeft(json));
                }
            });
            mSocket.on(EVENT_RIGHT, new SocketJsonListener(EVENT_RIGHT)
            {
                @Override
                protected void onReceive(String json, Object[] args)
                {
                    sendControlResultData(getEvent(), getControlSDK().moveRight(json));
                }
            });
            mSocket.on(EVENT_STOP_MOVE, new SocketJsonListener(EVENT_STOP_MOVE)
            {
                @Override
                protected void onReceive(String json, Object[] args)
                {
                    sendControlResultData(getEvent(), getControlSDK().stopMove(json));
                }
            });
            mSocket.on(EVENT_CATCH, new SocketJsonListener(EVENT_CATCH)
            {
                @Override
                protected void onReceive(String json, Object[] args)
                {
                    sendControlResultData(getEvent(), getControlSDK().doCatch(json));
                }
            });
            mSocket.on(EVENT_CHECK, new SocketJsonListener(EVENT_CHECK)
            {
                @Override
                protected void onReceive(String json, Object[] args)
                {
                    sendControlResultData(getEvent(), getControlSDK().check(json));
                }
            });

            //---------- WWControlParam end ----------

            WWLogger.get().log(Level.INFO, "Socket try connect:" + url + " " + mSocket);
            mSocket.connect();
        } catch (URISyntaxException e)
        {
            WWLogger.get().log(Level.SEVERE, "Socket connect error: " + e, e);
        }
    }

    public void disconnect()
    {
        if (mSocket != null)
        {
            WWLogger.get().log(Level.INFO, "Socket try disconnect:" + mSocket);
            WWControlSDK.getInstance().removeCallback(mControlSDKCallback);
            mSocket.disconnect();
            mSocket = null;
        }
    }

    private void sendControlResultData(String event, boolean result)
    {
        if (result)
        {
            sendData(event, SocketResponseModel.newOk(null));
        } else
        {
            sendData(event, SocketResponseModel.newErrorInternal(null));
        }
    }

    private boolean sendData(String event, Object data)
    {
        String dataString = null;
        if (data instanceof String)
        {
            dataString = (String) data;
        } else
        {
            dataString = WWJsonUtil.objectToJson(data);
        }

        final String prefix = "Socket sendData (" + event + ") ";

        if (isConnected())
        {
            Log.i(WWSocket.class.getSimpleName(), prefix + dataString);
            mSocket.emit(event, dataString);
            return true;
        } else
        {
            WWLogger.get().log(Level.SEVERE, prefix + "error socket not connected " + dataString);
            return false;
        }
    }

    private WWControlSDKCallback mControlSDKCallback = new WWControlSDKCallback()
    {
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
    };
}
