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
        super(getLogFilePath("wwserver", context),
                100 * MB,
                1,
                true);
    }

    public void addToLogger()
    {
        WWLogger.get().removeHandler(this);
        WWLogger.get().addHandler(this);
    }
}
