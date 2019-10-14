package com.example.temperaturepractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Exchange extends AppCompatActivity implements Runnable{
    private  static String TAG = "main";
    TextView out,input;
    Handler handler;
    float dollarRate;// = 0.1403f;
    float euroRate;// = 0.1278f;
    float wonRate;// = 167.9202f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        input = findViewById(R.id.input);
        out = findViewById(R.id.output);

        Intent intent = getIntent();
        /*dollarRate = intent.getFloatExtra("dollar",0.0f);
        euroRate = intent.getFloatExtra("euro",0.0f);
        wonRate = intent.getFloatExtra("won",0.0f);*/
        //开启子线程
        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==1){
                    dollarRate = (Float) msg.obj;
                    Log.i(TAG,"handleMessage: getMessage msg ="+ dollarRate);
                    //out.setText(str);
                }
                if(msg.what==2){
                    euroRate = (Float) msg.obj;
                    Log.i(TAG,"handleMessage: getMessage msg ="+ euroRate);
                }
                if(msg.what==3){
                    wonRate = (Float) msg.obj;
                    Log.i(TAG,"handleMessage: getMessage msg ="+ wonRate);
                }
                super.handleMessage(msg);
            }
        };

    }

    public static boolean isNumericZidai(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){

        }else if(item.getItemId()==R.id.open_list){
            //打开列表窗口
            Intent list = new Intent(this,MyListActivity.class);
            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }

    public void show(float a){
        String rmb = String.valueOf(input.getText());
        if (rmb.isEmpty()||!isNumericZidai(rmb)){
            Toast.makeText(this,"请输入正确的数字类型！",Toast.LENGTH_SHORT
            ).show();
        }
        else {
            float rm = Float.parseFloat(rmb);
            float result = rm * a;
            out.setText(String.format("%.2f", result));
        }
    }

    public void dollar(View v){
        show(dollarRate);
    }

    public void euro(View v){
        show(euroRate);
    }

    public void won(View v){
        show(wonRate);
    }

    public void conf(View v){
        Intent config = new Intent(this,ConfigActivity.class);
        config.putExtra("dollar",dollarRate);
        config.putExtra("euro",euroRate);
        config.putExtra("won",wonRate);
        Log.i(TAG,"handleMessage: getMessage msg1 ="+ dollarRate);
        Log.i(TAG,"handleMessage: getMessage msg2 ="+ euroRate);
        Log.i(TAG,"handleMessage: getMessage msg3 ="+ wonRate);

        startActivityForResult(config,1);
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==1 && resultCode==2){
            dollarRate = data.getFloatExtra("dollar",0.0f);
            euroRate = data.getFloatExtra("euro",0.0f);
            wonRate = data.getFloatExtra("won",0.0f);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void run(){
        //获取网络数据
        //URL url = null;
        String url = null;
        float dollar=0f,euro=0f,won=0f;
        try {
            //url = new URL("http://www.usd-cny.com/bankofchina.htm");
            /*HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG,"run: html = "+ html);*/

            url = "http://www.usd-cny.com/bankofchina.htm";
            Document doc = Jsoup.connect(url).get();
            Log.i(TAG,"run:"+doc.title());
            Elements tables = doc.getElementsByTag("table");

            Element table1 = tables.get(0);
            //获取TD中的数据
            Elements tds = table1.getElementsByTag("td");
            Element td1 = tds.get(6*6+5);
            String eu = td1.text();
            Log.i(TAG,"run: eu"+eu);
            Element td2 = tds.get(12*6+5);
            String wo = td2.text();
            Log.i(TAG,"run: wo"+wo);
            Element td3 = tds.get(25*6+5);
            String dol = td3.text();
            Log.i(TAG,"run: do"+dol);

            euro = 100f/Float.parseFloat(eu);
            won = 100f/Float.parseFloat(wo);
            dollar = 100f/Float.parseFloat(dol);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        Log.i(TAG,"run:run()...");
        /*for(int i = 1;i<6;i++){
            Log.i(TAG,"run:i=" + i);

            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }*/

        //获取Msg对象，用于返回主线程
        Message msg1 = handler.obtainMessage(1);
        //msg.what = 1;
        msg1.obj = dollar;
        Message msg2 = handler.obtainMessage(2);
        msg2.obj = euro;
        Message msg3 = handler.obtainMessage(3);
        msg3.obj = won;
        handler.sendMessage(msg1);
        handler.sendMessage(msg2);
        handler.sendMessage(msg3);
    }
    /*private  String inputStream2String(InputStream inputStream) throws IOException{
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        while (true){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz <0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }*/
}
