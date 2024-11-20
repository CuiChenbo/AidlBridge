package com.cuichen.aidlapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.cuichen.bridgeaidlsdk.CommBridgeManage
import com.cuichen.bridgeaidlsdk.ICommCallBackInterface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.act_main)
        onInit()
        initView()
    }

    val ifAction = "IntentFilter"
    private fun onInit() {
        CommBridgeManage.getInstance().init(this)
        CommBridgeManage.getInstance().setCommCallBackListener(commCall)

        var br = object :  BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                t_s?.text = intent?.getStringExtra("name")
            }
        }
       val itf = IntentFilter(ifAction)
        registerReceiver(br , itf)
    }

    var commCall = object : ICommCallBackInterface.Stub() {
        override fun requestData(data: String?) {
            t_c?.setText(data)
        }

        override fun removeData(data: String?) {
            t_c?.setText(data)
        }

    }

    var  t_c : TextView? = null
    var  t_s : TextView? = null

    private fun initView() {
        var et = findViewById<EditText>(R.id.et)
        t_c = findViewById<TextView>(R.id.tv_client)
        t_s = findViewById<TextView>(R.id.tv_service)
        findViewById<View>(R.id.bt_send).setOnClickListener {
            CommBridgeManage.getInstance().sendMsg(et.text.toString())
        }
    }

}
