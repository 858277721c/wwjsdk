package com.fanwe.lib.wwjsdk.sdk;

import android.content.Context;

import com.fanwe.lib.wwjsdk.log.DefaultFileHandler;
import com.fanwe.lib.wwjsdk.log.WWLogger;

import java.io.IOException;
import java.util.logging.Level;

/**
 * 娃娃sdk管理类
 */
public class WWSDKManager
{
    private static WWSDKManager sInstance;
    private Context mContext;
    private DefaultFileHandler mFileHandler;

    private WWSDKManager()
    {
    }

    public static WWSDKManager getInstance()
    {
        if (sInstance == null)
        {
            synchronized (WWSDKManager.class)
            {
                if (sInstance == null)
                {
                    sInstance = new WWSDKManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化娃娃sdk
     *
     * @param context
     */
    public final void init(Context context)
    {
        if (mContext == null)
        {
            mContext = context.getApplicationContext();

            DefaultFileHandler fileHandler = getFileHandler();
            if (fileHandler != null)
            {
                fileHandler.addToLogger();
                WWLogger.get().log(Level.INFO, "WWSDKManager init success");
            } else
            {
                WWLogger.get().log(Level.WARNING, "WWSDKManager init create FileHandler error");
            }
        }
    }

    private DefaultFileHandler getFileHandler()
    {
        if (mFileHandler == null)
        {
            try
            {
                mFileHandler = new DefaultFileHandler(mContext);
            } catch (IOException e)
            {
                e.printStackTrace();
                WWLogger.get().log(Level.SEVERE, e.toString(), e);
            }
        }
        return mFileHandler;
    }
}
