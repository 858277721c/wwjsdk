package com.fanwe.lib.wwjsdk.sdk.request;

/**
 * 娃娃机初始化控制参数
 */
public class WWInitParam
{
    /**
     * 设置本局游戏超时时间(单位秒)，超时后会自动下爪，默认60秒
     */
    public int timeout = 60;
    /**
     * 设置本局游戏下爪后，是否保持足够的爪力把娃娃抓起(1-保持爪力，如果为1的话，其他控制爪力的参数无效；0-不保持)，默认不保持
     */
    public int keepCatch = 0;
    /**
     * 单次移动爪子的时长(单位毫秒)，默认300毫秒
     */
    public int moveDuration = 300;

    /**
     * 抓起爪力[0-100]，默认值0表示由娃娃机内部控制
     */
    public int clawForceStart = 0;
    /**
     * 到顶爪力[0-100]，默认值0表示由娃娃机内部控制
     */
    public int clawForceTop = 0;
    /**
     * 移动爪力[0-100]，默认值0表示由娃娃机内部控制
     */
    public int clawForceMove = 0;
}
