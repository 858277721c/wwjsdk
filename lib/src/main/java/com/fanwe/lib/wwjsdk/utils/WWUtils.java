package com.fanwe.lib.wwjsdk.utils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */
public class WWUtils
{
    public static byte[] listToArray(List<Byte> list)
    {
        if (list == null || list.isEmpty())
        {
            return null;
        }
        final int size = list.size();
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++)
        {
            data[i] = list.get(i);
        }
        return data;
    }

    public static String byte2HexString(byte[] data, int length)
    {
        return byte2HexString(data, length, "_");
    }

    public static String byte2HexString(byte[] data, int length, String joinString)
    {
        if (data == null || length <= 0)
        {
            return "";
        }
        if (length > data.length)
        {
            length = data.length;
        }
        if (joinString == null)
        {
            joinString = "";
        }

        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < length; i++)
        {
            String hex = Integer.toHexString(data[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
            sb.append(joinString);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String getMacAddress()
    {
        try
        {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements())
            {
                NetworkInterface item = interfaces.nextElement();

                byte[] arrAddress = item.getHardwareAddress();
                if (arrAddress == null || arrAddress.length <= 0)
                {
                    continue;
                } else
                {
                    String address = byte2HexString(arrAddress, arrAddress.length, ":");
                    return address;
                }
            }
        } catch (SocketException e)
        {
            e.printStackTrace();
        }

        return "";
    }
}
