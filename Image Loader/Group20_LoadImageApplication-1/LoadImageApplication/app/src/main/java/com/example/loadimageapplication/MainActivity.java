package com.example.loadimageapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ExecutorService threadPool;
    ImageView imageView;
    Button asyncTaskBtn;
    Button threadBtn;
    String[] imageURL = new String[2];
    ProgressBar progressBar;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Display Image");

        progressBar = findViewById(R.id.progressBar);
        asyncTaskBtn = findViewById(R.id.button_AsyncTask);
        threadBtn = findViewById(R.id.button_Thread);
        imageView = findViewById(R.id.imageView);
        imageURL[0] = "https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg";
        imageURL[1] = "https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg";
        asyncTaskBtn.setBackgroundColor(getResources().getColor(R.color.black));
        asyncTaskBtn.setTextColor(getResources().getColor(R.color.white));
        threadBtn.setBackgroundColor(getResources().getColor(R.color.black));
        threadBtn.setTextColor(getResources().getColor(R.color.white));

        threadPool = Executors.newFixedThreadPool(2);

        asyncTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetImage().execute(imageURL[0]);
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                imageView.setImageBitmap((Bitmap) msg.obj);
                imageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        threadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Thread thread = new Thread(new DoWork(), "Worker1");
                //thread.start();
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                threadPool.execute(new DoWork(imageURL[1]));
            }
        });
    }

    Bitmap getImageBitmap(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    class GetImage extends AsyncTask<String, Integer, Bitmap> {
        public GetImage() {
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            Bitmap myBitmap = getImageBitmap(imageURL);
            return myBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(bitmap);
        }
    }

    public class DoWork implements Runnable {
        String imageUrl;

        public DoWork(String url) {
            this.imageUrl = url;
        }

        @Override
        public void run() {
            try {
                Bitmap imageBitmap = getImageBitmap(imageUrl);
                Message message = new Message();
                if (imageBitmap != null) message.obj = imageBitmap;
                handler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}