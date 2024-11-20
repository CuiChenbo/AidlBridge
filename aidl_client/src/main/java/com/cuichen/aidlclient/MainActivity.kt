package com.cuichen.aidlclient

import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cuichen.bridgeaidlsdk.CommBridgeManage
import com.cuichen.bridgeaidlsdk.ICommCallBackInterface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
     findViewById<View>(R.id.bt_bind).setOnClickListener {
         CommBridgeManage.getInstance().init(this)
     }
        findViewById<View>(R.id.bt_get).setOnClickListener {
            Log.i(TAG, "onCreate: ${CommBridgeManage.getInstance().info}")
        }
        findViewById<View>(R.id.bt_send).setOnClickListener {
            CommBridgeManage.getInstance().sendMsg("客户端发消息了")
        }
        findViewById<View>(R.id.bt_on_listener).setOnClickListener {
            CommBridgeManage.getInstance().setCommCallBackListener(commCall)
        }
    }


    val TAG = MainActivity::class.java.simpleName
    var commCall = object : ICommCallBackInterface.Stub() {
        override fun requestData(data: String?) {
            Log.i(TAG, "requestData: $data")
        }

        override fun removeData(data: String?) {
            Log.i(TAG, "removeData: $data")
        }

    }
}