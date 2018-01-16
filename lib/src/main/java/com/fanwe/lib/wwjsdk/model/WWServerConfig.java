package com.fanwe.lib.wwjsdk.model;

import android.os.Environment;

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

    public static final String KEY_SOCKET_URL = "key_socket_url";

    public String socketUrl;

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

        WWServerConfig config = new WWServerConfig();
        config.socketUrl = properties.getProperty(KEY_SOCKET_URL, null);
        return config;
    }

    private static Properties getProperties()
    {
        try
        {
            File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);

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
