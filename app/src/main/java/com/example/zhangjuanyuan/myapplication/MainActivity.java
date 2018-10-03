package com.example.zhangjuanyuan.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //設定字串陣列與圖片整數陣列要給GridView使用
    String title[] = {"雲端功能","紀錄功能","運動功能","提醒功能","相簿","計時功能" };
    int icon[] = {R.drawable.twotone_cloud,R.drawable.twotone_create,R.drawable.twotone_fitness,R.drawable.twotone_notifications
            ,R.drawable.twotone_photo,R.drawable.twotone_timer};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化gridview
        GridView gridView = findViewById(R.id.gridView);
        //產生Adapter物件
        IconAdapter iconAdapter = new IconAdapter();
        //將Adapter設給gridview
        gridView.setAdapter(iconAdapter);
        //設定清單物件的監聽事件處理
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //利用position判斷
                switch (position){
                    case 0:
                        Toast.makeText(MainActivity.this,title[position],Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this,title[position],Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this,title[position],Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this,title[position],Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this,title[position],Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        Toast.makeText(MainActivity.this,title[position],Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

    }
    //自訂一個內部class Adapter 並繼承BaseAdapter 實作四個方法
    class IconAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return title[position];
        }

        @Override
        public long getItemId(int position) {
            return icon[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            if(row==null){//判斷是否為第一次呼叫
                row = getLayoutInflater().inflate(R.layout.item_layout,null); //把R.layout.item_layout建立成一實際的View物件
                ImageView imageView = row.findViewById(R.id.img);
                TextView textView = row.findViewById(R.id.text);
                imageView.setImageResource(icon[position]);
                textView.setText(title[position]);
            }
            return row;
        }
    }
}
