package com.fanwe.lib.wwjsdk.sdk.serialport;

/**
 * 将控制参数转为串口字节数据
 */
public interface IWWSerialPortDataBuilder
{
    void init(String jsonInit);

    byte[] buildBegin(String jsonString);

    byte[] buildMove(String jsonString, Direction direction);

    byte[] buildStopMove(String jsonString);

    byte[] buildCatch(String jsonString);

    byte[] buildCheck(String jsonString);

    enum Direction
    {
        Front,
        Back,
        Left,
        Right,
    }
}
