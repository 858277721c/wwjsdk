package com.fanwe.lib.wwjsdk.log;

import android.content.Context;

import com.fanwe.lib.log.FFileHandler;

import java.io.IOException;

/**
 * 默认的log文件保存处理类
 */
public class DefaultFileHandler extends FFileHandler
{
    public DefaultFileHandler(Context context) throws IOException
    {
        super("wwserver",
                100 * MB,
                context);
    }

    public void addToLogger()
    {
        WWLogger.get().removeHandler(this);
        WWLogger.get().addHandler(this);
    }
}
