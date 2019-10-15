package com.example.temperaturepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class onRate extends AppCompatActivity {

    TextView name,outp;
    EditText inp;

    float value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_rate);

        name = findViewById(R.id.name);
        inp = findViewById(R.id.rate_input);
        outp = findViewById(R.id.rate_result);

        Intent intent = getIntent();
        String title = intent.getStringExtra("name");
        value = Float.parseFloat(intent.getStringExtra("value"));

        name.setText(title);

        inp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String s1 = inp.getText().toString();
                show(s1,value);
            }
        });
    }
    public void show(String rmb,float a){
        if (rmb.isEmpty()||!isNumericZidai(rmb)){
            Toast.makeText(this,"请输入正确的数字类型！",Toast.LENGTH_SHORT
            ).show();
        }
        else {
            float rm = Float.parseFloat(rmb);
            float result = rm * a;
            outp.setText(String.format("%.2f", result));
        }
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
}
