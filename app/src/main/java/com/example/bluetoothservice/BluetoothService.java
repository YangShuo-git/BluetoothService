package com.example.bluetoothservice;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class BluetoothService extends Service {
    private Timer timer; // 定时器，创建一个线程
    public BluetoothService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        checkBluetooth();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void  checkBluetooth(){
        if (timer == null){
            timer = new Timer();

            // 设置定时任务（线程）
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    // 创建消息对象
                    Message msg = MainActivity.handler.obtainMessage();

                    // 需要将消息先封装到bundle中
                    Bundle bundle = new Bundle();

                    // 获取蓝牙状态信息
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter != null){
                        boolean flag = bluetoothAdapter.isEnabled();
                        if (flag){
                            bundle.putString("status", "蓝牙已经打开");
                        } else {
                            bundle.putString("status", "蓝牙已经关闭");
                        }
                    } else {
                        bundle.putString("status", "蓝牙异常");
                    }

                    // 发送消息对象，发送成功的话，就会回调handleMessage()
                    msg.setData(bundle);
                    MainActivity.handler.sendMessage(msg);
                }
            };

            // 延迟1s开始监测，之后每隔5s执行一次
            timer.schedule(task,1*1000,1*1000);
        }
    }
}