package com.example.loadimageapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.net.URL;

public class DoWork implements Runnable{
    private Handler handler;
    String imageUrl;
    public DoWork(String url) {
        this.imageUrl = url;
    }

    @Override
    public void run() {
        try{
            URL url = new URL(imageUrl);
            Bitmap image = BitmapFactory.decodeStream(url.openStream());
        } catch(Exception e)
        {
            sendMsg("Failed Downloading");
        }

    }

    private void sendMsg(String myMessage) {
        Bundle bundle = new Bundle();
        bundle.putString("status", myMessage);
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
