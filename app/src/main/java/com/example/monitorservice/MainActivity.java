package com.example.monitorservice;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static TextView tv_blueStatus;
    private static TextView tv_netStatus;
    private BluetoothService.BluetoothBinder bluetoothBinder;
    private NetService.NetBinder netBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_monitor);
        Log.d(TAG, "onCreate");

        setTitle("功能检测");
        tv_blueStatus = findViewById(R.id.tv_blueStatus);
        tv_netStatus = findViewById(R.id.tv_netStatus);
    }

    private ServiceConnection mBluetoothConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 绑定服务成功后回调  立马获取binder对象
            Log.d(TAG, "onServiceConnected....");
            bluetoothBinder = (BluetoothService.BluetoothBinder) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接后回调
            Log.d(TAG, "onServiceDisconnected.....");
            bluetoothBinder = null;
        }
    };

    private ServiceConnection mNetConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            netBinder = (NetService.NetBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            netBinder = null;
        }
    };

    // 点击按钮，绑定服务
    public void clickService(View view){
        if (view.getId() == R.id.btn_check) {
            Log.d(TAG, "启动检测服务....");

            Intent intentBluetooth = new Intent(this, BluetoothService.class);
            // bindService会拉起服务，新起一个线程，但仍然在同一个进程中
            bindService(intentBluetooth, mBluetoothConnection, BIND_AUTO_CREATE);

            Intent intentNet = new Intent(this, NetService.class);
            bindService(intentNet, mNetConnection, BIND_AUTO_CREATE);
        } else {
            Log.d(TAG, "没有控件....");
        }
    }

    // Activity和Service两者是线程间通信，所以需要使用Handler
    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            // 1、先获取bundle，后续再通过key值取出信息
            Bundle bundle = msg.getData();

            // 格式化时间
            Date currentDate = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(currentDate);

            // 2、一个Handler处理多个消息时，可以使用不同的消息标识符（what）来区分不同的消息，复用Handler
            switch (msg.what) {
                case 1:
                    // 3、通过bundle的key值，取出对应的消息
                    String msg_bluetooth = bundle.getString("blue_status");
                    tv_blueStatus.setText(strDate + " " + msg_bluetooth);
                    break;
                case 2:
                    String msg_net = bundle.getString("net_status");
                    tv_netStatus.setText(strDate + " " + msg_net);
            }
        }
    };
}