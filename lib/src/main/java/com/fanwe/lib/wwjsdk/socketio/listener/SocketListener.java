package com.fanwe.lib.wwjsdk.socketio.listener;

import io.socket.emitter.Emitter;

/**
 * Created by Administrator on 2017/12/22.
 */
public abstract class SocketListener implements Emitter.Listener
{
    private String mEvent;

    public SocketListener(String event)
    {
        mEvent = event;
    }

    public final String getEvent()
    {
        return mEvent;
    }
}
