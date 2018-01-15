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
     * 参数错误
     */
    public static final int CODE_PARAM_ERROR = 100;
    /**
     * app内部错误
     */
    public static final int CODE_INTERNAL_ERROR = 101;

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

    public static SocketResponseModel newInternalError(String msg)
    {
        if (msg == null)
        {
            msg = "app internal error";
        }

        SocketResponseModel model = new SocketResponseModel();
        model.code = CODE_INTERNAL_ERROR;
        model.msg = msg;
        return model;
    }

    public static SocketResponseModel newParamError(String msg)
    {
        if (msg == null)
        {
            msg = "param error";
        }

        SocketResponseModel model = new SocketResponseModel();
        model.code = CODE_PARAM_ERROR;
        model.msg = msg;
        return model;
    }
}
