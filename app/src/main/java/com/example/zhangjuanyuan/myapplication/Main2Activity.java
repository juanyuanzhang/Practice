package com.example.zhangjuanyuan.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();//加入物件ㄜokhttpclient

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Request request = new Request.Builder()
                .url("http://atm201605.appspot.com/h")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() { //進行排程連線
            @Override
            public void onFailure(Call call, IOException e) {
                //告知使用者連線失敗

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String json = response.body().string();
                Log.d("okhttp",json);
                parseJSON(json);  //解析json自訂方法

            }
        });
    }
    private void parseJSON(String s){
        ArrayList<Transaction> trans = new ArrayList<>();
        try{
            JSONArray array = new JSONArray(s);
            for(int i=0 ; i<array.length() ; i++){
                JSONObject obj = array.getJSONObject(i);
                String account = obj.getString("account");
                String date = obj.getString("date");
                int amount = obj.getInt("amount");
                int type = obj.getInt("type");
                Log.d("JSON2",account+"/"+date+"/"+amount+"/"+type);
                Transaction t = new Transaction(account,date,amount,type);
                trans.add(t);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
