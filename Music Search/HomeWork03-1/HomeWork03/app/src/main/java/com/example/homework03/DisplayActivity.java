package com.example.homework03;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DisplayActivity extends AppCompatActivity {

    private ImageView ivArtWork;
    private TextView tvTrack;
    private TextView tvGenre;
    private TextView tvArtist;
    private TextView tvAlbumName;
    private TextView tvTrackPrice;
    private TextView tvAlbumPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        setTitle("iTunes Music Search");

        ivArtWork = findViewById(R.id.imageView);
        tvTrack = findViewById(R.id.tvTrackDispData);
        tvGenre = findViewById(R.id.tvGenreData);
        tvArtist = findViewById(R.id.tvArtistDispData);
        tvAlbumName = findViewById(R.id.tvAlbumDispData);
        tvTrackPrice = findViewById(R.id.tvTrackPriceDipsData);
        tvAlbumPrice = findViewById(R.id.tvAlbumPriceDispData);

        if(getIntent() != null && getIntent().getExtras() != null){

            Music music = (Music) getIntent().getExtras().getSerializable(MainActivity.MUSIC_KEY);
            Log.d("demo", "Data on intent display activity: " + music.toString());

            tvTrack.setText(music.titleTrack);
            tvGenre.setText(music.genre);
            tvArtist.setText(music.artistName);
            tvAlbumName.setText(music.albumName);
            tvTrackPrice.setText(music.trackPrice);
            tvAlbumPrice.setText(music.albumPrice);

            if (isConnected()) {
                new DisplayActivity.GetImageAsync(ivArtWork).execute(music.trackImage);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        }

        findViewById(R.id.btnFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class GetImageAsync extends AsyncTask<String, Void, Void> {
        Bitmap bitmap = null;
        ImageView imageView;

        public GetImageAsync(ImageView iv) {
            imageView = iv;
        }

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection connection = null;
            bitmap = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (bitmap != null && ivArtWork != null) {
                ivArtWork.setImageBitmap(bitmap);
            }
        }
    }
}
