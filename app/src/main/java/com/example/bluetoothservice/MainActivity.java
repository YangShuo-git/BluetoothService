package com.example.bluetoothservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static TextView tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bluetooth);

        setTitle("检测蓝牙");
        tv_status = (TextView)findViewById(R.id.tv_status);
        tv_status.setText("...");
    }

    // 点击按钮，启动服务
    public void clickBluetooth(View view){
        if (R.id.btn_check == view.getId()){
            Intent intent = new Intent(this, BluetoothService.class);
            Toast.makeText(MainActivity.this, "启动蓝牙检测服务", Toast.LENGTH_LONG).show();
            startService(intent);
        } else {
            Toast.makeText(MainActivity.this, "No control", Toast.LENGTH_LONG).show();
        }
    }

    // 接收服务传来的消息
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            // 先获取bundle，再通过key值解出信息
            Bundle bundle = msg.getData();
            String msg_bluetooth = bundle.getString("status");

            // 时间格式化
            Date currentDate = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(currentDate);

            // 显示解出来的蓝牙状态信息
            tv_status.setText(strDate + " " + msg_bluetooth);
        }
    };
}