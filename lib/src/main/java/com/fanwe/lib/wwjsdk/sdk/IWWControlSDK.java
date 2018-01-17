package com.fanwe.lib.wwjsdk.sdk;

import com.fanwe.lib.wwjsdk.sdk.callback.WWControlSDKCallback;
import com.fanwe.lib.wwjsdk.sdk.request.WWInitParam;

/**
 * 娃娃机控制sdk
 */
public interface IWWControlSDK
{
    /**
     * 添加回调对象
     *
     * @param callback
     */
    void addCallback(WWControlSDKCallback callback);

    /**
     * 移除回调对象
     *
     * @param callback
     */
    void removeCallback(WWControlSDKCallback callback);

    /**
     * 初始化本局游戏的参数配置
     *
     * @param param 本局游戏的控制参数
     */
    void init(WWInitParam param);

    /**
     * 开始控制娃娃机
     *
     * @return
     */
    boolean begin(String jsonString);

    /**
     * 向前移动爪子
     *
     * @return
     */
    boolean moveFront(String jsonString);

    /**
     * 向后移动爪子
     *
     * @return
     */
    boolean moveBack(String jsonString);

    /**
     * 向左移动爪子
     *
     * @return
     */
    boolean moveLeft(String jsonString);

    /**
     * 向右移动爪子
     *
     * @return
     */
    boolean moveRight(String jsonString);

    /**
     * 停止移动爪子
     *
     * @return
     */
    boolean stopMove(String jsonString);

    /**
     * 下爪
     *
     * @return
     */
    boolean doCatch(String jsonString);

    /**
     * 检测娃娃机的状态
     *
     * @return
     */
    boolean check(String jsonString);

    /**
     * 向串口发送数据
     *
     * @param data 串口数据
     * @param desc 附加描述
     * @return
     */
    boolean sendData(byte[] data, String desc);

    /**
     * 销毁sdk
     */
    void onDestroy();

    IWWControlSDK EMPTY = new IWWControlSDK()
    {
        @Override
        public void addCallback(WWControlSDKCallback callback)
        {
        }

        @Override
        public void removeCallback(WWControlSDKCallback callback)
        {
        }

        @Override
        public void init(WWInitParam param)
        {
        }

        @Override
        public boolean begin(String jsonString)
        {
            return false;
        }

        @Override
        public boolean moveFront(String jsonString)
        {
            return false;
        }

        @Override
        public boolean moveBack(String jsonString)
        {
            return false;
        }

        @Override
        public boolean moveLeft(String jsonString)
        {
            return false;
        }

        @Override
        public boolean moveRight(String jsonString)
        {
            return false;
        }

        @Override
        public boolean stopMove(String jsonString)
        {
            return false;
        }

        @Override
        public boolean doCatch(String jsonString)
        {
            return false;
        }

        @Override
        public boolean check(String jsonString)
        {
            return false;
        }

        @Override
        public boolean sendData(byte[] data, String desc)
        {
            return false;
        }

        @Override
        public void onDestroy()
        {
        }
    };
}
