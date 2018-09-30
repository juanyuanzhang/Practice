package com.example.zhangjuanyuan.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;


public class MyIntentService extends IntentService {

    public static final String PARAM_MSG = "msg";

//必須將基本建構子寫出，不然AndroidManifest.xml宣告服務會有錯誤
    public MyIntentService(){
        super("MyIntentService");

    }
    //繼承IntentService必須實作的方法，無論重複呼叫幾次此服務，他會自動以內建的佇列(Queue)依序處理Intent物件
    @Override
    protected void onHandleIntent(Intent intent) {
        String msg = intent.getStringExtra(PARAM_MSG);
        SystemClock.sleep(3000);
        //顯示系統時間，其實也可以不用，主要看處理的時間差
        String debug = DateFormat.format("hh:mm:ss", System.currentTimeMillis())+"\t"+msg;
        Log.d("MyIntentService",debug);

    }
}
