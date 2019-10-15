package com.example.temperaturepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {
    ArrayList<HashMap<String,String>> listItems;
    SimpleAdapter listItemsAdapter;
    Handler handler;
    ListView listView;
    private String TAG = "Mylist";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        listView = findViewById(R.id.list);
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
                    listItems = new ArrayList<HashMap<String, String>>();
                    for (i = 0;i<27;i++){
                        HashMap<String,String> map = new HashMap<String, String>();
                        String str = list2.get(i);
                        String[] str1 = new String[2];
                        str1 = str.split(",");
                        map.put("ItemTitle",str1[0]);//标题文字
                        map.put("ItemDetail",str1[1]);//详情描述
                        listItems.add(map);
                    }
                    listItemsAdapter = new SimpleAdapter(MyListActivity.this,
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

        listView.setOnItemClickListener(this);
        listView.setEmptyView(findViewById(R.id.nodata));
        listView.setOnItemLongClickListener(this);
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
            Thread.sleep(30);
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Elements tables = doc.getElementsByTag("table");

            Element table1 = tables.get(0);
            Elements tds = table1.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);

                String str1 = td1.text();
                String val = td2.text();
                float val1 = 100f/Float.parseFloat(val);
                String val2 = String.valueOf(val1);
                keyList.add(str1+","+val2);
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

    @Override
    public void onItemClick(AdapterView<?> parent,View view,
                            int position,long id){
        Object itemAtPosition = listView.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String, String>) itemAtPosition;
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");

        TextView title = view.findViewById(R.id.itemTitle);
        TextView detail = view.findViewById(R.id.itemDetail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());

        Intent item = new Intent(this,onRate.class);
        item.putExtra("name",title2);
        item.putExtra("value",detail2);
        startActivity(item);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int in) {
                        listItems.remove(i);
                        listItemsAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}
