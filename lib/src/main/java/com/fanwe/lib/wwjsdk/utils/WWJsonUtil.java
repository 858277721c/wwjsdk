package com.fanwe.lib.wwjsdk.utils;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/12/21.
 */
public final class WWJsonUtil
{
    private static final Gson GSON = new Gson();

    public static String objectToJson(Object object)
    {
        return GSON.toJson(object);
    }

    public static <T> T jsonToObject(String json, Class<T> clazz)
    {
        try
        {
            return GSON.fromJson(json, clazz);
        } catch (Exception e)
        {
            return null;
        }
    }
}
