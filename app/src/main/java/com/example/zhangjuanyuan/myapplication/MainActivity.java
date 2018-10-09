package com.example.zhangjuanyuan.myapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,
                                                                MediaPlayer.OnErrorListener,
                                                                MediaPlayer.OnCompletionListener{
    private MediaPlayer mMediaPlayer = null ;
    private Boolean mbIsInitialised = true ; //判斷是否需要執行prepareAsync()
    ImageButton btnPause ,btnStop ,btnGoto;
    ToggleButton btnRepeat;
    EditText edtGoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //初始化
        findView();
        btnPause.setOnClickListener(btnPauseOnClick);
        btnStop.setOnClickListener(btnStopOnClick);
        btnRepeat.setOnClickListener(btnRepeatOnClick);
        btnGoto.setOnClickListener(btnGotoOnClick);
    }
   //在onResume()方法中設定MediaPlayer物件，利用setDataSource()指定播放的檔案
    @Override
    protected void onResume() {
        super.onResume();

        mMediaPlayer = new MediaPlayer();
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.song);

        try{
            mMediaPlayer.setDataSource(this,uri);

        } catch (IOException e) {
            Toast.makeText(MainActivity.this,"指定的音樂檔錯誤",Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }finally {

        }
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }
    //onStop要清除不用的MediaPlayer()物件
    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.release();
        mMediaPlayer=null;
    }
    public void findView(){
        btnPause = findViewById(R.id.btnPause);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnStop = findViewById(R.id.btnStop);
        btnGoto = findViewById(R.id.btnGoto);
        edtGoto = findViewById(R.id.edtGoto);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //MediaPlayer播放完畢後會執行這個方法
        btnPause.setImageResource(android.R.drawable.ic_media_play); //將暫停圖片改成play圖

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //prepareAsync()執行過程中出現錯誤時會呼叫這個方法
        mMediaPlayer.release();
        mMediaPlayer= null;

        Toast.makeText(MainActivity.this,"發生錯誤，停止撥放",Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //prepareAsync()執行完成後會呼叫這個方法
        mMediaPlayer.seekTo(0); //  將音樂時間調至0秒
        mMediaPlayer.start();

        Toast.makeText(MainActivity.this, "開始播放", Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener btnPauseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mMediaPlayer.isPlaying()){
                btnPause.setImageResource(android.R.drawable.ic_media_play);
                mMediaPlayer.pause();
            }else{
                btnPause.setImageResource(android.R.drawable.ic_media_pause);
                if(mbIsInitialised){
                    mMediaPlayer.prepareAsync();
                    mbIsInitialised = false ;
                }else {
                    mMediaPlayer.start();
                }
            }
        }
    };

    private View.OnClickListener btnStopOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMediaPlayer.stop();

            //停止播放後必須在執行prepareAsync() 或prepare() 才能重播
            mbIsInitialised = true ;
            btnPause.setImageResource(android.R.drawable.ic_media_play);
        }
    };

    private View.OnClickListener btnRepeatOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(((ToggleButton)v).isChecked())
                mMediaPlayer.setLooping(true);
            else
                mMediaPlayer.setLooping(false);

        }
    };

    private View.OnClickListener btnGotoOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtGoto.getText().toString().equals("")) {
                Toast.makeText(MainActivity.this, "請先輸入要播放的位置", Toast.LENGTH_LONG).show();
                return;
            }
            int seconds = Integer.parseInt(edtGoto.getText().toString());
            mMediaPlayer.seekTo(seconds * 1000);
        }
    };
}
