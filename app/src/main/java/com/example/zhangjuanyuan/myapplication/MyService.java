package com.example.zhangjuanyuan.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private final String LOG_TAG = "Service";

    //內部類別回傳MyService物件用
    public class LocalBinder extends Binder{
        MyService getService(){
            return MyService.this;
        }
    }
    private LocalBinder localBinder = new LocalBinder();

    public void myMethod(){//連結Service才能使用的方法
        Log.d(LOG_TAG,"myMethod()");
    }


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG,"onBinder()");
        return localBinder;
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG,"onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG,"onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG,"onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG,"onUnbinder()");
        return super.onUnbind(intent);
    }
}
