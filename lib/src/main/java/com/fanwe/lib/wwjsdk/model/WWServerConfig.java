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
    public static final String KEY_PORT_PATH = "key_port_path";
    public static final String KEY_PORT_BAUDRATE = "key_port_baudrate";

    public String initUrl;
    public String portPath;
    public int portBaudRate;

    private WWServerConfig()
    {
    }

    public static WWServerConfig get()
    {
        final Properties properties = getProperties();
        if (properties == null)
        {
            return null;
        }

        WWServerConfig config = new WWServerConfig();

        final String initUrl = properties.getProperty(KEY_INIT_URL, null);
        if (TextUtils.isEmpty(initUrl))
        {
            WWLogger.get().log(Level.SEVERE, "key_init_url's value is empty");
        } else
        {
            config.initUrl = initUrl;
        }

        final String portPath = properties.getProperty(KEY_PORT_PATH, null);
        if (TextUtils.isEmpty(portPath))
        {
            WWLogger.get().log(Level.SEVERE, "key_port_path's value is empty");
        } else
        {
            config.portPath = portPath;
        }

        final String portBaudRate = getProperties().getProperty(KEY_PORT_BAUDRATE, null);
        if (TextUtils.isEmpty(portBaudRate))
        {
            WWLogger.get().log(Level.SEVERE, "key_port_baudrate's value is empty");
        } else
        {
            if (TextUtils.isDigitsOnly(portBaudRate))
            {
                config.portBaudRate = Integer.valueOf(portBaudRate);
            } else
            {
                WWLogger.get().log(Level.SEVERE, "key_port_baudrate's value must be digit");
            }
        }

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
