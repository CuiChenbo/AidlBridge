package com.cuichen.aidlapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.cuichen.bridgeaidlsdk.ICommAidlBridge
import com.cuichen.bridgeaidlsdk.ICommCallBackInterface

class CommService : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    val TAG = "CommService"

    var commCallBack: ICommCallBackInterface? = null

    val bind : ICommAidlBridge.Stub = object : ICommAidlBridge.Stub() {
        override fun setOnICommCallBackInterface(commCall: ICommCallBackInterface?) {
            Log.i(TAG, "setOnICommCallBackInterface: ${commCall?.hashCode()}")
            commCallBack = commCall;
            commCallBack?.requestData("监听回调已建立")
            commCallBack?.removeData("监听回调已建立")
        }

        override fun sendMsg(msg: String?) {
            val i = Intent("IntentFilter")
            i.putExtra("name" , msg)
            sendBroadcast(i)
            commCallBack?.requestData("Service:收到消息( $msg )")
        }

        override fun getInfo(): String {
            return "Service-getInfo"
        }

    }

    override fun onBind(intent: Intent): IBinder {
        return bind
    }
}