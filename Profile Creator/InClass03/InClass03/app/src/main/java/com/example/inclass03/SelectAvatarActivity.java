package com.example.inclass03;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SelectAvatarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);
        setTitle("Select Avatar");

        findViewById(R.id.imageView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.REQUEST_VALUE, R.drawable.avatar_f_1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.REQUEST_VALUE, R.drawable.avatar_m_1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.REQUEST_VALUE, R.drawable.avatar_f_2);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.REQUEST_VALUE, R.drawable.avatar_m_2);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.REQUEST_VALUE, R.drawable.avatar_f_3);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        findViewById(R.id.imageView6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.REQUEST_VALUE, R.drawable.avatar_m_3);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
