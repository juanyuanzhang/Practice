package com.example.zhangjuanyuan.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button button ;
    //練習IntentService使用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this , MyIntentService.class);
                intent1.putExtra(MyIntentService.PARAM_MSG, "Test1");
                startService(intent1);

                Intent intent2 = new Intent(MainActivity.this , MyIntentService.class);
                intent2.putExtra(MyIntentService.PARAM_MSG, "Test2");
                startService(intent2);

                Intent intent3 = new Intent(MainActivity.this , MyIntentService.class);
                intent3.putExtra(MyIntentService.PARAM_MSG , "Test3");
                startService(intent3);
            }
        });

    }
}
