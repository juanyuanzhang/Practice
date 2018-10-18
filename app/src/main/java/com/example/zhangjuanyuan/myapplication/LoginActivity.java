package com.example.zhangjuanyuan.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d("onAuthStateChanged","登入:"+user.getUid());
                    userUID = user.getUid();
                }else {
                    Log.d("onAuthStateChanged","已登出");
                }
            }
        };
        Button button = findViewById(R.id.btnlogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

    }
    public void login(View view){
        String email = ((EditText)findViewById(R.id.editText)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText2)).getText().toString();
        Log.d("AUTH",email+"/"+password);
        auth.signInWithEmailAndPassword(email,password);
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
    }
}
