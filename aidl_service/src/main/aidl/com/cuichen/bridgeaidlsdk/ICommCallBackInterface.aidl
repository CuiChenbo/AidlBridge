// ICommCallBackInterface.aidl
package com.cuichen.bridgeaidlsdk;

// Declare any non-default types here with import statements

interface ICommCallBackInterface {

   void requestData(in String data);

   void removeData(in String data);
}