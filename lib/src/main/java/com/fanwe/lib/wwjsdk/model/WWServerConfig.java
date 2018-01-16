package com.fanwe.lib.wwjsdk.model;

import android.os.Environment;
import android.text.TextUtils;

import com.fanwe.lib.wwjsdk.log.WWLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * 配置实体
 */
public class WWServerConfig
{
    public static final String FILE_NAME = "fanwe_wwserver.txt";

    public static final String KEY_INIT_URL = "key_init_url";

    public String initUrl;

    private WWServerConfig()
    {
    }

    public static WWServerConfig get()
    {
        Properties properties = getProperties();
        if (properties == null)
        {
            return null;
        }
        String initUrl = properties.getProperty(KEY_INIT_URL, null);
        if (TextUtils.isEmpty(initUrl))
        {
            WWLogger.get().log(Level.SEVERE, "key_init_url's value is empty");
            return null;
        }

        WWServerConfig config = new WWServerConfig();
        config.initUrl = initUrl;
        return config;
    }

    private static Properties getProperties()
    {
        try
        {
            File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
            if (file == null || !file.exists())
            {
                WWLogger.get().log(Level.SEVERE, "getProperties error: " + FILE_NAME + " not exist");
                return null;
            }

            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            return properties;
        } catch (IOException e)
        {
            WWLogger.get().log(Level.SEVERE, "getProperties error:" + e, e);
            return null;
        }
    }
}
