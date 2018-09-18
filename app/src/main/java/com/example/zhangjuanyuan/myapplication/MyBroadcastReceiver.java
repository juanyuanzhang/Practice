package com.example.zhangjuanyuan.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver{//接收動態廣播後執行
    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra("data");
        Toast.makeText(context,data,Toast.LENGTH_LONG).show();
    }
}
