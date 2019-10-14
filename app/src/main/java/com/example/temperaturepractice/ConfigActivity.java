package com.example.temperaturepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ConfigActivity extends AppCompatActivity {
    private  static String TAG = "main";
    TextView dollar,euro,won;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent = getIntent();
        dollar = findViewById(R.id.exdollar);
        euro = findViewById(R.id.exeuro);
        won = findViewById(R.id.exwon);



        float exdollar = intent.getFloatExtra("dollar",0.0f);
        float exeuro = intent.getFloatExtra("euro",0.0f);
        float exwon = intent.getFloatExtra("won",0.0f);

        /*SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
        float exdollar = sharedPreferences.getFloat("dollar_rate",0.0f);
        float exeuro = sharedPreferences.getFloat("euro_rate",0.0f);
        float exwon = sharedPreferences.getFloat("won_rate",0.0f);
        Log.i(TAG, "handleMessage: getMessage msg1 =" + exdollar);
        Log.i(TAG,"handleMessage: getMessage msg2 ="+ exeuro);
        Log.i(TAG,"handleMessage: getMessage msg3 ="+ exwon);*/


        dollar.setText(String.valueOf(exdollar));
        euro.setText(String.valueOf(exeuro));
        won.setText(String.valueOf(exwon));

    }

    public void save(View v){

        float dollarRate = Float.parseFloat(String.valueOf(dollar.getText()));
        float euroRate = Float.parseFloat(String.valueOf(euro.getText()));
        float wonRate = Float.parseFloat(String.valueOf(won.getText()));

        Intent save = new Intent(this,Exchange.class);
        Bundle bdl = new Bundle();
        bdl.putFloat("dollar",dollarRate);
        bdl.putFloat("euro",euroRate);
        bdl.putFloat("won",wonRate);
        save.putExtras(bdl);

        /*SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("dollar_rate",dollarRate);
        editor.putFloat("euro_rate",euroRate);
        editor.putFloat("won_rate",wonRate);
        editor.apply();*/

        save.putExtra("dollar",dollarRate);
        save.putExtra("euro",euroRate);
        save.putExtra("won",wonRate);
        setResult(2,save);

        finish();
        startActivity(save);
    }
}
