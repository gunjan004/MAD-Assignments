package com.example.Gunjan.hw04;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Generatedp extends AppCompatActivity {

    ArrayList<String> apass = new ArrayList<>();
    ArrayList<String> tpass = new ArrayList<>();
    LayoutInflater inflater;
    LinearLayout linearThread;
    LinearLayout linearAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generatedp);

        setTitle("Generated Passwords");

        //Change
        linearThread = findViewById(R.id.linearThread);
        linearAsync = findViewById(R.id.linearAsync);
        inflater = getLayoutInflater();
        View viewThread;
        View viewAsync;

        apass = getIntent().getExtras().getStringArrayList("async");
        tpass = getIntent().getExtras().getStringArrayList("thread");


        for (String stThread : tpass) {

            viewThread = inflater.inflate(R.layout.text_layout, linearThread, false);

            // In order to get the view we have to use the new view with text_layout in it
            TextView textView_Thread = viewThread.findViewById(R.id.text);
            textView_Thread.setText(stThread);

            // Add the text view to the parent layout
            linearThread.addView(textView_Thread);
        }

        for (String stAsync : apass) {
            viewAsync = inflater.inflate(R.layout.text_layout, linearAsync, false);
            TextView textView_Async = viewAsync.findViewById(R.id.text);
            textView_Async.setText(stAsync);
            linearAsync.addView(textView_Async);
        }

        Button finish = findViewById(R.id.btn_finish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Generatedp.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}
