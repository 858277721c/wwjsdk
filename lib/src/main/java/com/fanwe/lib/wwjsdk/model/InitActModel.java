package com.fanwe.lib.wwjsdk.model;

/**
 * Created by Administrator on 2018/1/16.
 */
public class InitActModel
{
    private int status;
    private String error;

    private int type; // 类型 （0：使用腾讯云；1：自己的socket服务器）
    private String socket_address; // 链接服务端的socket服务器地址

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getSocket_address()
    {
        return socket_address;
    }

    public void setSocket_address(String socket_address)
    {
        this.socket_address = socket_address;
    }
}
