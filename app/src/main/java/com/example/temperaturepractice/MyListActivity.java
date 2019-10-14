package com.example.temperaturepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements Runnable{
    ArrayList<HashMap<String,String>> listItems;
    //SimpleAdapter listItemsAdapter;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        final ListView listView = findViewById(R.id.mylist);
       // String data[] = {"111","2222"};

        listItems = new ArrayList<HashMap<String, String>>();

        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 6) {
                    List<String> list2 = (List<String>)msg.obj;
                    int i;
                    ArrayList<HashMap<String,String>> listItems = new ArrayList<HashMap<String, String>>();
                    for (i = 0;i<27;i++){
                        HashMap<String,String> map = new HashMap<String, String>();
                        String str = list2.get(i);
                        String[] str1 = new String[2];
                        str1 = str.split(",");
                        map.put("ItemTitle",str1[0]);//标题文字
                        map.put("ItemDetail",str1[1]);//详情描述
                        listItems.add(map);
                    }
                    SimpleAdapter listItemsAdapter = new SimpleAdapter(MyListActivity.this,
                            listItems,//listItems数据源
                            R.layout.list_item,//list_item的XML布局实现
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail}
                    );
                    listView.setAdapter(listItemsAdapter);
                }
                super.handleMessage(msg);
            }
        };

        /*for(int i = 0;i<10;i++){
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("ItemTitle","Rate: " + i);//标题文字
            map.put("ItemDetail","detail" + i);//详情描述
            listItems.add(map);
        }

        //生成适配器的Item和动态数组对应的元素
        listItemsAdapter = new SimpleAdapter(this,
                listItems,//listItems数据源
                R.layout.list_item,//list_item的XML布局实现
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle,R.id.itemDetail}
                 );


        //ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(listItemsAdapter);*/

    }

    @Override
    public void run() {
        //获取网络数据，放入List带回到主线程中
        List<String> keyList = new ArrayList<String>();
        Document doc = null;
        try{
            Thread.sleep(300);
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Elements tables = doc.getElementsByTag("table");

            Element table1 = tables.get(0);
            Elements tds = table1.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);

                String str1 = td1.text();
                String val = td2.text();
                keyList.add(str1+","+val);
            }
        }catch(IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage(6);
        msg.obj = keyList;
        handler.sendMessage(msg);
    }

}
