package com.example.zhangjuanyuan.myapplication;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    private String userUID;  //用來存使用者的UID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();  //取得 FirebaseAuth物件 登入與認證功能
        authStateListener = new FirebaseAuth.AuthStateListener() { //監聽者，監聽使用者登入狀況
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); //取得FirebaseUser物件
                if(user != null){
                    Log.d("onAuthStateChanged","登入:"+user.getUid());
                    userUID = user.getUid(); //取得使用者的UID方法
                }else {
                    Log.d("onAuthStateChanged","已登出");
                }
            }
        };
        //登入按鈕事件處理
        Button button = findViewById(R.id.btnlogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

    }
    //登入事件處理
    public void login(View view){
        final String email = ((EditText)findViewById(R.id.editText)).getText().toString(); //取得email字串
        final String password = ((EditText)findViewById(R.id.editText2)).getText().toString(); //取得密碼字串
        Log.d("AUTH",email+"/"+password);
        //使用者登入(email&password)監聽是否成功
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){ //利用task的isSuccessful()方法(布林值)判斷登入是否成功
                    Log.d("onComplete","登入失敗");
                    register(email,password); //自訂登入失敗呼叫註冊視窗方法
                }
            }
        });
    }
    //詢問是否註冊方法
    public void register(final String email , final String password){
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("登入問題")
                .setMessage("無此帳號，是否需要以此信箱與密碼註冊新帳號")
                .setPositiveButton("註冊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createUser(email,password); //自訂註冊方法
                    }
                })
                .setNeutralButton("取消",null)
                .show();
    }
    //註冊使用者方法，並告知否註冊成功
    public void createUser(String email, String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String message = task.isComplete() ? "註冊成功" : "註冊失敗" ;
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage(message)
                        .setPositiveButton("OK",null)
                        .show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
        auth.signOut(); //關閉App時登出
    }
}
