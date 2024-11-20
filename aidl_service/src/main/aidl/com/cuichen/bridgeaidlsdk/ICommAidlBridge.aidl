// ICommAidlBridge.aidl
package com.cuichen.bridgeaidlsdk;

// Declare any non-default types here with import statements
import com.cuichen.bridgeaidlsdk.ICommCallBackInterface;

interface ICommAidlBridge {

    void setOnICommCallBackInterface(ICommCallBackInterface commCall);

    void sendMsg(String msg);

    String getInfo();
}