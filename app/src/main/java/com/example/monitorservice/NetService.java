package com.example.monitorservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class NetService extends Service {
    private static final String TAG = "NetService";
    private Timer timer;
    private NetBinder netBinder = new NetBinder();

    public NetService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        checkNet();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return netBinder;
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
    }

    public class NetBinder extends Binder {

    }

    public void checkNet() {
        if (timer == null) {
            timer = new Timer();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    // 获取网络信息
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();

                    Message msg = MainActivity.handler.obtainMessage(MainActivity.MSG_NET);
                    Bundle bundle = new Bundle();
                    if (netInfo != null && netInfo.isConnected()) {
                        // 网络已连接
                        bundle.putString("net_status", "网络已连接");
                    } else {
                        // 网络已断开
                        bundle.putString("net_status", "网络已断开");
                    }

                    msg.setData(bundle);
                    MainActivity.handler.sendMessage(msg);
                    Log.d(TAG, "checkNet(): " + msg);
                }
            };

            timer.schedule(task, 1 * 1000, 1 * 1000);
        }
    }
}