package com.example.Gunjan.hw04;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public int counter = 1;
    private SeekBar tc;
    private SeekBar tl;
    private SeekBar ac;
    private SeekBar al;
    ArrayList<String> apasswords;
    ArrayList<String> tpasswords = new ArrayList<>();
    public int itc = 1;
    public int itl = 7;
    public int iac = 1;
    public int ial = 7;
    Handler handler;
    private TextView sdtc;
    private TextView sdtl;
    private TextView sdac;
    private TextView sdal;
    private Button generate;
    public ProgressDialog progressDialog;
    ExecutorService threadpool;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Password Generator");

        tc = findViewById(R.id.seekBar_tc);
        tl = findViewById(R.id.seekBar_tl);
        ac = findViewById(R.id.seekBar_ac);
        al = findViewById(R.id.seekBar_al);

        generate = findViewById(R.id.button_generate);

        sdtc = findViewById(R.id.textView_sdtc);
        sdtc.setText(String.valueOf(1));

        sdtl = findViewById(R.id.textView_sdtl);
        sdtl.setText(String.valueOf(7));

        sdac = findViewById(R.id.textView_sdac);
        sdac.setText(String.valueOf(1));

        sdal = findViewById(R.id.textView_sdal);
        sdal.setText(String.valueOf(7));

        threadpool = Executors.newFixedThreadPool(2);

        tc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                itc = progress + 1;

                Log.d("itc", " " + itc);
                sdtc.setText(String.valueOf(itc));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                itl = progress + 7;
                sdtl.setText(String.valueOf(itl));
                Log.d("itl", " " + itl);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ac.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iac = progress + 1;
                sdac.setText(String.valueOf(iac));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        al.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ial = progress + 7;
                sdal.setText(String.valueOf(ial));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                threadpool.execute(new Thread(new ThreadTask()));

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Updating Progress");
                progressDialog.setMax(iac + itc);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();

            }
        });

        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 1) {
                    new Async().execute(iac, ial);
                }
                return false;
            }
        });

    }

    class ThreadTask implements Runnable{

        @Override
        public void run() {

            for (int i = 0; i < itc; i++) {
                tpasswords.add(Util.getPassword(itl));
                progressDialog.setProgress(counter++);
            }

            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
            Log.d("array333"," "+tpasswords);
        }
    }

    class Async extends AsyncTask<Integer, Void, ArrayList<String>> {

        @Override
        protected void onPostExecute(ArrayList<String> strings) {

            apasswords = strings;
            progressDialog.dismiss();
            Intent i = new Intent(MainActivity.this,Generatedp.class);
            i.putExtra("async",apasswords);
            i.putExtra("thread",tpasswords);
            startActivity(i);
            Log.d("array"," "+apasswords);
        }

        @Override
        protected ArrayList<String> doInBackground(Integer... params) {
            ArrayList<String> result = new ArrayList<>();

            for (int i = 0; i < params[0]; i++) {
                result.add(Util.getPassword(params[1]));
                progressDialog.setProgress(counter++);
            }
            return result;
        }

    }
}