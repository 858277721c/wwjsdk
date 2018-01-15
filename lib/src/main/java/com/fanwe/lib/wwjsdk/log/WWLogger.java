package com.fanwe.lib.wwjsdk.log;

import com.fanwe.lib.log.FLogger;

import java.util.logging.Logger;

/**
 * Created by Administrator on 2017/12/11.
 */
public class WWLogger extends FLogger
{
    public static Logger get()
    {
        return get("WWServer");
    }
}
