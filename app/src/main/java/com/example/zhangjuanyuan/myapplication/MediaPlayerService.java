package com.example.zhangjuanyuan.myapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.Toast;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener,MediaPlayer.OnCompletionListener,
        AudioManager.OnAudioFocusChangeListener
{

    private MediaPlayer mediaPlayer =null; //程式使用之MediaPlayer物件
    private boolean mbIsInitialised = true , mbAudioFileFound = false ; //用來記錄是否MediaPlayer物件需要執行prepareAsync()

    public static final String ACTION_PLAY = "com.example.zhangjuanyuan.myapplication.action.PLAY"
            , ACTION_PAUSE = "com.example.zhangjuanyuan.myapplication.action.PAUSE"
            , ACTION_SET_REPEAT = "com.example.zhangjuanyuan.myapplication.action.SET_REPEAT"
            , ACTION_CANCEL_REPEAT = "com.example.zhangjuanyuan.myapplication.action.CANCEL_REPEAT"
            , ACTION_GOTO = "com.example.zhangjuanyuan.myapplication.action.GOTO";
    public MediaPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    //設定MediaPlayer物件的功能，如之前的onResume()
    @Override
    public void onCreate() {
        super.onCreate();
        ContentResolver contentResolver = getContentResolver();
        String[] columes = {
                MediaStore.MediaColumns.TITLE, MediaStore.MediaColumns._ID
        };
        Cursor c = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columes, null, null, null);

        Uri uri = null ;
        if(c == null){
            Toast.makeText(MediaPlayerService.this, "Content Resolver  錯誤! ", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!c.moveToFirst()){
            Toast.makeText(MediaPlayerService.this, "資料庫沒有資料",Toast.LENGTH_LONG).show();
            return;
        }
        else {
            do{
                String title = c.getString(c.getColumnIndex(MediaStore.MediaColumns.TITLE));
                if(title.equals("myMP3")){
                    mbAudioFileFound = true ;
                    break;
                }
            }while (c.moveToNext());
            if(!mbAudioFileFound){
                Toast.makeText(MediaPlayerService.this ,"找不到指定的檔案",Toast.LENGTH_LONG).show();
                return;
            }
            int idColumn = c.getColumnIndex(MediaStore.Audio.Media._ID);
            long id = c.getLong(idColumn);
            uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try{
            mediaPlayer.setDataSource(this , uri);
        }catch (Exception e){
            Toast.makeText(MediaPlayerService.this, "指定的播放檔錯誤", Toast.LENGTH_LONG).show();
        }
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //根據主程式傳送過來的Intent物件中的指令控制MediaPlayer的運作
        if(!mbAudioFileFound) {
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        if(intent.getAction().equals(ACTION_PLAY)){
            if(mbIsInitialised){
                mediaPlayer.prepareAsync();
                mbIsInitialised = false ;
            }else {
                mediaPlayer.start();
            }
        }else if(intent.getAction().equals(ACTION_PAUSE)){
            mediaPlayer.pause();
        }else if(intent.getAction().equals(ACTION_SET_REPEAT)){
            mediaPlayer.setLooping(true);
        }else if(intent.getAction().equals(ACTION_CANCEL_REPEAT)){
            mediaPlayer.setLooping(false);
        }else if(intent.getAction().equals(ACTION_GOTO)){
            int seconds = intent.getIntExtra("GOTO_SECONDS",0);
            mediaPlayer.seekTo(seconds * 1000);
        }

        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //清除MediaPlayer物件EX:onStop()
        if(mbAudioFileFound){
            mediaPlayer.release();
            mediaPlayer = null ;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //執行完MediaPlayer後會執行此方法
        mediaPlayer.release();
        mediaPlayer = null ;

        stopForeground(true);

        mbIsInitialised = true ;


    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mediaPlayer.release();
        mediaPlayer = null ;
        Toast.makeText(MediaPlayerService.this , "發生錯誤，停止撥放",Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //是否取得 audio focus
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int r = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if(r != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) mediaPlayer.setVolume(0.1f,0.1f);
        mediaPlayer.start();

        //啟動MediaPlayer 物件開始播放音樂
        Intent it = new Intent(getApplicationContext(),Main2Activity.class) ;
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, it, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setTicker("播放背景音樂")
                .setContentTitle(getString(R.string.app_name))
                .setContentText("背景音樂播放中.......")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1 ,notification);
        Toast.makeText(MediaPlayerService.this,"開始播放",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange){
            case AudioManager.AUDIOFOCUS_GAIN:
                //程式取得聲音播放權
                mediaPlayer.setVolume(0.8f,0.8f);
                mediaPlayer.start();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                //程式尚失聲音播放權，而且時間可能很久
                stopSelf();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                //程式尚失聲音撥放權，但預期很快就會在取得
                if(mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                //程式尚失聲音撥放權，但可以用很小的音量繼續播放
                if(mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f,0.1f);
                break;
        }
    }
}
