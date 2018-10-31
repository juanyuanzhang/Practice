package com.example.zhangjuanyuan.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private Button btnStart ,btnPause ,btnStop ,btnRepeat ,btnCancelRep ,btnTime ,btnAddM ;
    private EditText editTime ;

    private final int REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findView();

    }
    //處理每個按鈕事件處理
    @Override
    public void onClick(View v) {
        Intent it ;
        switch (v.getId()){
            case R.id.btnStart:
                it = new Intent(Main2Activity.this , MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_PLAY);
                startService(it);
                break;
            case R.id.btnPause:
                it = new Intent(Main2Activity.this , MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_PAUSE);
                startService(it);
                break;
            case R.id.btnStop:
                it = new Intent(Main2Activity.this , MediaPlayerService.class);
                stopService(it);
                startService(it);
                break;
            case R.id.btnRepeat:
                it = new Intent(Main2Activity.this , MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_SET_REPEAT);
                startService(it);
                break;
            case R.id.btnCancelRep:
                it = new Intent(Main2Activity.this , MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_CANCEL_REPEAT);
                startService(it);
                break;
            case R.id.btnTime:
                if(editTime.getText().toString().equals("")){
                    Toast.makeText(Main2Activity.this, "請先輸入播放位置(單位:秒)", Toast.LENGTH_LONG).show();
                    break;
                }
                int seconds = Integer.parseInt(editTime.getText().toString());

                it = new Intent(Main2Activity.this , MediaPlayerService.class);
                it.setAction(MediaPlayerService.ACTION_GOTO);
                it.putExtra("GOTO_SECONDS" ,seconds);
                startService(it);
                break;

        }

    }

    public void findView(){
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnCancelRep = findViewById(R.id.btnCancelRep);
        btnTime = findViewById(R.id.btnTime);
        btnAddM = findViewById(R.id.btnAddM);
        editTime = findViewById(R.id.editTime);

        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);
        btnCancelRep.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnAddM.setOnClickListener(btnAddMediaOnClick);

        askForWriteExternalStoragePermission();
    }

    private View.OnClickListener btnAddMediaOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //檢查是否取得權限
            if(ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                ContentValues val = new ContentValues();
                val.put(MediaStore.MediaColumns.TITLE,"myMP3");
                val.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
                val.put(MediaStore.MediaColumns.DATA,"/sdcard/myMP3.mp3");
                ContentResolver contentResolver = getContentResolver();
                Uri uri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,val);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));
            }else {
                askForWriteExternalStoragePermission();
            }

        }
    };
    private void askForWriteExternalStoragePermission() {
        if(ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
    PackageManager.PERMISSION_GRANTED){
            //這項功能尚未取得使用者的同意
            //開始詢問使用者的流程
            if(ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Main2Activity.this)
                        .setTitle("提示")
                        .setMessage("App需要SD卡中的資料")
                        .setCancelable(false)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //顯示詢問使用者是否同意功能權限的對話
                                //使用者答覆後會執行onRequestPermissionsResult()
                                ActivityCompat.requestPermissions(Main2Activity.this ,new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST);

                            }
                        });
                alertDialog.show();
                return;
            } else {
                ActivityCompat.requestPermissions(Main2Activity.this ,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //檢查收到的要求編號是否和我們送出的相同
        if(requestCode == REQUEST ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(Main2Activity.this, "取得SD CARD 權限", Toast.LENGTH_LONG).show();
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
