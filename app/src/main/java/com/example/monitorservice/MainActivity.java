package com.example.monitorservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static TextView tv_blueStatus;
    private static TextView tv_netStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_monitor);

        setTitle("功能检测");
        tv_blueStatus = findViewById(R.id.tv_blueStatus);
        tv_netStatus = findViewById(R.id.tv_netStatus);

        tv_blueStatus.setText("...");
        tv_netStatus.setText("...");
    }

    // 点击按钮，启动服务
    public void clickService(View view){
        if (R.id.btn_check == view.getId()){
            Toast.makeText(MainActivity.this, "启动检测服务", Toast.LENGTH_LONG).show();

            Intent intentBlue = new Intent(this, BluetoothService.class);
            startService(intentBlue);

            Intent intentNet = new Intent(this, NetService.class);
            startService(intentNet);
        } else {
            Toast.makeText(MainActivity.this, "No control", Toast.LENGTH_LONG).show();
        }
    }

    // 接收服务传来的消息
    public static Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            // 先获取bundle，后续再通过key值取出信息
            Bundle bundle = msg.getData();

            // 格式化时间
            Date currentDate = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(currentDate);

            // 在一个 Handler 中处理多个消息时，可以使用不同的消息标识符（what）来区分不同的消息
            switch (msg.what) {
                case 1:
                    // 通过key值，取出对应的消息
                    String msg_bluetooth = bundle.getString("blue_status");
                    // 显示信息
                    tv_blueStatus.setText(strDate + " " + msg_bluetooth);
                    break;
                case 2:
                    String msg_net = bundle.getString("net_status");
                    tv_netStatus.setText(strDate + " " + msg_net);
            }
        }
    };
}