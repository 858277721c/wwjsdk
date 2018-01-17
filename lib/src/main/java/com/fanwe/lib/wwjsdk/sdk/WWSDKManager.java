package com.fanwe.lib.wwjsdk.sdk;

import android.content.Context;

import com.fanwe.lib.log.FFileHandler;
import com.fanwe.lib.log.FLogger;
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

    public Context getContext()
    {
        checkInit();
        return mContext;
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
            if (context == null)
            {
                throw new NullPointerException("context is null");
            }
            mContext = context.getApplicationContext();

            DefaultFileHandler fileHandler = getFileHandler();
            if (fileHandler != null)
            {
                fileHandler.addToLogger();
            }

            WWLogger.get().log(Level.INFO, "WWSDKManager init finish");
        }
    }

    /**
     * 检查sdk是否初始化，如果未初始化会抛异常
     */
    public void checkInit()
    {
        if (mContext == null)
        {
            throw new RuntimeException("you must invoke WWSDKManager.getInstance().init(context) before this");
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
                WWLogger.get().log(Level.SEVERE, "create FileHandler error" + e, e);
            }
        }
        return mFileHandler;
    }

    /**
     * 默认的log文件保存处理类
     */
    private class DefaultFileHandler extends FFileHandler
    {
        public DefaultFileHandler(Context context) throws IOException
        {
            super("wwserver",
                    100 * MB,
                    context);
        }

        public void addToLogger()
        {
            FLogger.removeHandler(DefaultFileHandler.class, WWLogger.get());
            WWLogger.get().addHandler(this);
        }
    }
}
