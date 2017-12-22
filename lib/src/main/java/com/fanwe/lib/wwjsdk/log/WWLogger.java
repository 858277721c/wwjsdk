package com.fanwe.lib.wwjsdk.log;

import java.util.logging.Logger;

/**
 * Created by Administrator on 2017/12/11.
 */
public class WWLogger
{
    private static final String TAG_DEFAULT = "WWServer";

    public static Logger get()
    {
        return Logger.getLogger(TAG_DEFAULT);
    }
}
