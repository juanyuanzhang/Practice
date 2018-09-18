package com.example.zhangjuanyuan.myapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn1,btn2,btn3,btn4;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findview();

        btn1.setOnClickListener(new View.OnClickListener() { //動態註冊
            @Override
            public void onClick(View v) {
                IntentFilter intentFilter = new IntentFilter("com.example.zhangjuanyuan.myapplication");
                myBroadcastReceiver = new MyBroadcastReceiver();
                registerReceiver(myBroadcastReceiver, intentFilter);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //動態註冊註銷
                unregisterReceiver(myBroadcastReceiver);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //動態註冊傳送廣播
                Intent intent = new Intent("com.example.zhangjuanyuan.myapplication");
                intent.putExtra("data","test");
                sendBroadcast(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() { //靜態註冊傳送廣播
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.android.myBroadcast2");
                intent.putExtra("data2","TEST2");
                sendBroadcast(intent);

            }
        });



    }
    public void findview(){
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        btn3=findViewById(R.id.btn3);
        btn4=findViewById(R.id.btn4);
    }
}
