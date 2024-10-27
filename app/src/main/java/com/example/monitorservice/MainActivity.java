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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static TextView tv_blueStatus;
    private static TextView tv_netStatus;
    private BluetoothService.BluetoothBinder bluetoothBinder;
    private NetService.NetBinder netBinder;

    public static final int MSG_BLUETOOTH = 1;
    public static final int MSG_NET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_monitor);
        setTitle("功能检测");

        tv_blueStatus = findViewById(R.id.tv_blueStatus);
        tv_netStatus = findViewById(R.id.tv_netStatus);
    }

    public void clickService(View view){
        if (view.getId() == R.id.btn_check) {
            Log.d(TAG, "启动检测服务....");
            // bindService会拉起服务，这里是新起一个线程
            Intent intentBluetooth = new Intent(this, BluetoothService.class);
            bindService(intentBluetooth, mBluetoothConnection, BIND_AUTO_CREATE);

            Intent intentNet = new Intent(this, NetService.class);
            bindService(intentNet, mNetConnection, BIND_AUTO_CREATE);
        } else {
            Log.d(TAG, "没有控件....");
        }
    }

    private ServiceConnection mBluetoothConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 绑定服务成功后回调  立马获取binder对象
            Log.d(TAG, "Bluetooth onServiceConnected....");
            bluetoothBinder = (BluetoothService.BluetoothBinder) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接后回调
            Log.d(TAG, "Bluetooth onServiceDisconnected.....");
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

    // Activity和Service两者是线程间通信，需要使用Handler
    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData(); // 1、先获取bundle
            // Handler处理多个消息时，可以使用不同的消息标识符（what）来区分不同的消息
            switch (msg.what) {
                case MSG_BLUETOOTH:
                    handleOnBluetoothChanged(bundle); // 2、再通过bundle的key值，取出对应的消息并处理
                    break;
                case MSG_NET:
                    handleOnNetChanged(bundle);
                    break;
                default:
                    Log.e(TAG, "msg is mismatch!");
            }
        }

        private void handleOnBluetoothChanged (Bundle bundle) {
            String msg_bluetooth = bundle.getString("blue_status");
            tv_blueStatus.setText(getDate() + " " + msg_bluetooth);
        }

        private void handleOnNetChanged (Bundle bundle) {
            String msg_net = bundle.getString("net_status");
            tv_netStatus.setText(getDate() + " " + msg_net);
        }

        private String getDate() {
            // 格式化时间
            Date currentDate = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return dateFormat.format(currentDate);
        }
    };
}