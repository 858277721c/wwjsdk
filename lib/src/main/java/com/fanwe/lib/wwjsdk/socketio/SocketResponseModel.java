package com.fanwe.lib.wwjsdk.socketio;

/**
 * Created by Administrator on 2017/12/19.
 */

public class SocketResponseModel
{
    /**
     * ok状态
     */
    public static final int CODE_OK = 0;
    /**
     * app内部错误
     */
    public static final int CODE_ERROR_INTERNAL = 100;
    /**
     * 参数错误
     */
    public static final int CODE_ERROR_PARAM = 101;

    public int code;
    public String msg;
    public Object data;

    public static SocketResponseModel newOk(String msg)
    {
        if (msg == null)
        {
            msg = "ok";
        }
        SocketResponseModel model = new SocketResponseModel();
        model.code = CODE_OK;
        model.msg = msg;
        return model;
    }

    public static SocketResponseModel newErrorInternal(String msg)
    {
        if (msg == null)
        {
            msg = "app internal error";
        }
        SocketResponseModel model = new SocketResponseModel();
        model.code = CODE_ERROR_INTERNAL;
        model.msg = msg;
        return model;
    }

    public static SocketResponseModel newErrorParam(String msg)
    {
        if (msg == null)
        {
            msg = "param error";
        }
        SocketResponseModel model = new SocketResponseModel();
        model.code = CODE_ERROR_PARAM;
        model.msg = msg;
        return model;
    }
}
