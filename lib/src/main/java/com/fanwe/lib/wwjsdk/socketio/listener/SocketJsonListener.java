package com.fanwe.lib.wwjsdk.socketio.listener;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/22.
 */
public abstract class SocketJsonListener extends SocketListener
{
    public SocketJsonListener(String event)
    {
        super(event);
    }

    @Override
    public final void call(Object... args)
    {
        String json = argsToJson(args);
        onReceive(json, args);
    }

    /**
     * 收到json数据
     *
     * @param json
     * @param args
     */
    protected abstract void onReceive(String json, Object... args);

    public static String argsToJson(Object... args)
    {
        if (args == null || args.length <= 0)
        {
            return null;
        }
        Object object = args[0];
        if (!(object instanceof JSONObject))
        {
            return null;
        }
        JSONObject jsonObject = (JSONObject) object;
        return jsonObject.toString();
    }
}
