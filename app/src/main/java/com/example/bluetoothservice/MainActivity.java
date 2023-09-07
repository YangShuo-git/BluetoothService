package com.example.bluetoothservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        tv_status = findViewById(R.id.tv_status);
    }

    // 点击按钮，启动服务
    public void clickBluetooth(View view){
        if (R.id.btn_check == view.getId()){
//            Intent intent = new
        } else {
            Toast.makeText(MainActivity.this, "No control", Toast.LENGTH_LONG).show();
        }
    }

    // 接收服务传来的消息
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            String msg_bluetooth = bundle.getString("status");

            Date currentDate = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(currentDate);

            tv_status.setText(strDate + " " + msg_bluetooth);
        }
    };
}