package com.fanwe.lib.wwjsdk.xuebao;

import com.fanwe.lib.wwjsdk.model.WWServerConfig;
import com.fanwe.lib.wwjsdk.sdk.WWControlSDK;
import com.fanwe.lib.wwjsdk.sdk.serialport.WWSerialPort;
import com.fanwe.lib.wwjsdk.sdk.serialport.WWSerialPortDataBuilder;

/**
 * 雪暴娃娃机控制sdk
 */
public class XueBaoWWControlSDK extends WWControlSDK
{
    @Override
    protected WWSerialPortDataBuilder provideSerialDataBuilder()
    {
        return new XueBaoWWSerialPortDataBuilder();
    }

    @Override
    protected WWSerialPort provideSerialPort()
    {
        WWSerialPort serialPort = new XueBaoWWSerialPort();

        WWServerConfig config = WWServerConfig.get();
        String portPath = config.portPath;
        int portBaudRate = config.portBaudRate;

        serialPort.init(portPath, portBaudRate);
        return serialPort;
    }
}
