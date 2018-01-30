package com.fanwe.lib.wwjsdk.utils;

import com.fanwe.lib.wwjsdk.log.WWLogger;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;

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

    /**
     * 按比例把原值转换为目标值
     *
     * @param source             要转换的原值，必须大于0
     * @param sourceMax          原值最大值，必须大于0
     * @param targetMax          目标值最大值，必须大于0
     * @param valueForScaledZero 如果缩放后的值为0，则返回当前参数
     * @return
     */
    public static int scaleValue(int source, int sourceMax, int targetMax, int valueForScaledZero)
    {
        if (source <= 0 || sourceMax <= 0 || targetMax <= 0)
        {
            return 0;
        }

        float percent = source / ((float) sourceMax);
        if (percent > 1f)
        {
            percent = 1f;
        }

        final float value = percent * targetMax;
        int result = Math.round(value);
        if (result <= 0)
        {
            result = valueForScaledZero;
        }
        return result;
    }

    public static String getMacAddress(int index)
    {
        List<String> listAddress = getMacAddress();
        try
        {
            return listAddress.get(index);
        } catch (Exception e)
        {
            return "";
        }
    }

    public static List<String> getMacAddress()
    {
        List<String> listAddress = new ArrayList<>();
        try
        {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements())
            {
                final NetworkInterface item = interfaces.nextElement();
                final byte[] arrAddress = item.getHardwareAddress();
                if (arrAddress != null && arrAddress.length > 0)
                {
                    String address = byte2HexString(arrAddress, arrAddress.length, ":");
                    listAddress.add(address);
                }
            }
            if (listAddress.isEmpty())
            {
                WWLogger.get().log(Level.WARNING, "getMacAddress fail not found");
            }
            return listAddress;
        } catch (Exception e)
        {
            WWLogger.get().log(Level.SEVERE, "getMacAddress error:" + e, e);
            return listAddress;
        }
    }
}
