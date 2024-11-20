package com.cuichen.bridgeaidlsdk;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class CommBridgeManage {

    private static CommBridgeManage commBridgeManage;

    public static CommBridgeManage getInstance(){
        if(commBridgeManage == null){
            synchronized (CommBridgeManage.class){
                if (commBridgeManage == null){
                    commBridgeManage = new CommBridgeManage();
                }
                return commBridgeManage;
            }
        }else{
            return commBridgeManage;
        }
    }

    private Context mContext;
    private final String TAG = CommBridgeManage.class.getSimpleName();
    public void init(Context context){
        this.mContext = context;
        connect();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidlBridge = ICommAidlBridge.Stub.asInterface(service);
            if(saveICommCallBackInterface != null){
                setCommCallBackListener(saveICommCallBackInterface);
                saveICommCallBackInterface = null;
            }
            if(saveMsg != null){
               sendMsg(saveMsg);
               saveMsg = null;
            }
            try {
                aidlBridge.asBinder().linkToDeath(deathRecipient , 0);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            close();
        }

    };

    ICommAidlBridge aidlBridge;

    private void connect(){
        Intent intent = new Intent();
        intent.setPackage(mContext.getPackageName());
        intent.setAction("com.cuichen.aidlbridge.CommService");
       boolean isBind = mContext.bindService(intent , connection , Service.BIND_AUTO_CREATE);
        Log.i(TAG, "connect: "+isBind);
    }

    private ICommCallBackInterface saveICommCallBackInterface;//预存回调，防止未连接时就设置回调监听
    public void setCommCallBackListener(ICommCallBackInterface call){
        if (mContext == null) {
            Log.i(TAG, "need to initialize first");
            return;
        }
        if(isConnected()){
            try {
                aidlBridge.setOnICommCallBackInterface(call);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }else {
            saveICommCallBackInterface = call;
        }
    }

    private String saveMsg;
    public void sendMsg(String msg){
        if (mContext == null) {
            Log.i(TAG, "need to initialize first");
            return;
        }
        if(isConnected()) {
            try {
                aidlBridge.sendMsg(msg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }else {
            saveMsg = msg;
        }
    }

    public String getInfo(){
        if (mContext == null) {
            Log.i(TAG, "need to initialize first");
            return null;
        }
        try {
            return aidlBridge.getInfo();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private Object mLock = new Object();
    public boolean isConnected() {
        synchronized (mLock) {
            return aidlBridge != null && aidlBridge.asBinder().isBinderAlive();
        }
    }

   final IBinder.DeathRecipient deathRecipient = this::close;

    public void close(){
        ICommAidlBridge iCommAidlBridge = null;
        if(aidlBridge !=null){
            mContext.unbindService(connection);
            iCommAidlBridge = aidlBridge;
            aidlBridge = null;
        }
        try {
            iCommAidlBridge.asBinder().linkToDeath(deathRecipient , 0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
