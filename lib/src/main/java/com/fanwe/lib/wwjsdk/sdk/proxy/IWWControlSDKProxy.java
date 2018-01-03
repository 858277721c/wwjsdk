package com.fanwe.lib.wwjsdk.sdk.proxy;

import com.fanwe.lib.wwjsdk.sdk.callback.WWControlSDKCallback;

/**
 * 娃娃机控制sdk，对外控制接口
 */
public interface IWWControlSDKProxy
{
    /**
     * 设置本局游戏下爪后，是否保持足够的爪力把娃娃抓起
     *
     * @param keepCatch 1-保持爪力，0-不保持
     */
    void init(int keepCatch);

    /**
     * 设置本局游戏下爪后，保持足够的爪力把娃娃抓起的概率
     *
     * @param numerator   分子
     * @param denominator 分母(大于0)
     */
    void init(int numerator, int denominator);

    /**
     * 设置回调对象
     *
     * @param callback {@link WWControlSDKCallback}
     */
    void setCallback(WWControlSDKCallback callback);

    /**
     * 开始控制娃娃机
     *
     * @return
     */
    boolean begin();

    /**
     * 向前移动爪子
     *
     * @return
     */
    boolean moveFront();

    /**
     * 向后移动爪子
     *
     * @return
     */
    boolean moveBack();

    /**
     * 向左移动爪子
     *
     * @return
     */
    boolean moveLeft();

    /**
     * 向右移动爪子
     *
     * @return
     */
    boolean moveRight();

    /**
     * 停止移动爪子
     *
     * @return
     */
    boolean stopMove();

    /**
     * 下爪
     *
     * @return
     */
    boolean doCatch();

    /**
     * 检测娃娃机的状态
     *
     * @return
     */
    boolean check();
}
