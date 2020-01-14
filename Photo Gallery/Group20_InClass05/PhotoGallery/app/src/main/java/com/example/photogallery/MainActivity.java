package com.example.photogallery;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button goButton;
    private ArrayList<String> keywords;
    private String selectedKeyword;
    private AlertDialog.Builder builder;
    private TextView textView;
    private int imgIndex;
    private Bitmap DisplayImage;
    private ImageView selectedImage;
    private ImageView prevImage;
    private ImageView nextImage;
    private ProgressBar progressBar;
    private ArrayList<String> urlArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goButton = findViewById(R.id.go_btn);
        textView = findViewById(R.id.textView_searchID);
        selectedImage = findViewById(R.id.imageView_MainID);
        prevImage = findViewById(R.id.imageView_Prev);
        nextImage = findViewById(R.id.imageView_Next);
        progressBar = findViewById(R.id.progressBar);
        prevImage.setEnabled(false);
        nextImage.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);

        if(isConnected()) {
            new GetKeywords().execute("http://dev.theappsdr.com/apis/photos/keywords.php");
        }
        else{
            Toast.makeText(MainActivity.this, "NO Internet Please Check Connection", Toast.LENGTH_LONG).show();
        }

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });

        prevImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp;
                Log.d("demo", "image imdex old : " + imgIndex);
                if(imgIndex == 0) {
                    temp = urlArray.size() - 1;
                    imgIndex = temp;
                }else{
                    temp = imgIndex - 1;
                    imgIndex = temp;
                }
                Log.d("demo", "image imdex new: " + imgIndex);
                progressBar.setVisibility(View.VISIBLE);
                prevImage.setEnabled(false);
                nextImage.setEnabled(false);
                new GetImages(selectedImage).execute(urlArray.get(imgIndex));
            }
        });

        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp;
                Log.d("demo", "image imdex old : " + imgIndex);
                Log.d("demo", "Array: " + urlArray.size());
                if(imgIndex < urlArray.size()-1){
                    temp = (Integer)imgIndex + 1;
                    imgIndex = temp;
                    Log.d("demo", "temp: " + temp);
                    Log.d("demo", "image index new: " + imgIndex);
                    Log.d("demo", "Array: " + urlArray.size());
                    progressBar.setVisibility(View.VISIBLE);
                    prevImage.setEnabled(false);
                    nextImage.setEnabled(false);
                    new GetImages(selectedImage).execute(urlArray.get(imgIndex));
                }
                else{
                    temp = 0;
                    imgIndex = temp;
                    progressBar.setVisibility(View.VISIBLE);
                    prevImage.setEnabled(false);
                    nextImage.setEnabled(false);
                    new GetImages(selectedImage).execute(urlArray.get(imgIndex));
                }
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

    private class GetKeywords extends AsyncTask<String, Void, ArrayList<String>>{


        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            URL url;

            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        String[] str = line.split(";");
                        keywords = new ArrayList<String>(Arrays.asList(str));
                        Log.d("demo", "keywords : " + line);
                        //stringBuilder.append(line);
                    }
                    //keywords = stringBuilder.toString();
                    return keywords;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return keywords;
        }

        @Override
        protected void onPostExecute(ArrayList<String> arrayList) {
            super.onPostExecute(arrayList);
            if(arrayList.size() > 0){
                Log.d("demo", arrayList.toString());
            }else{
                Log.d("demo", "null result");
                Toast.makeText(MainActivity.this, "No Keywords found", Toast.LENGTH_LONG).show();
            }
            final CharSequence[] options = arrayList.toArray(new CharSequence[arrayList.size()]);
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose a Keyword");
            builder.setCancelable(false);
            builder.setItems(options, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Log.d("demo", "onClick" + options[item]);
                    textView.setText(options[item]);
                    selectedKeyword = (String) options[item];
                    progressBar.setVisibility(View.VISIBLE);
                    goButton.setEnabled(false);
                    new GetURL().execute("http://dev.theappsdr.com/apis/photos/index.php?keyword=" + selectedKeyword);
                }
            });
            //builder.show();
            Log.d("Demo", options.toString());
        }
    }
    class GetURL extends AsyncTask<String, Void, ArrayList<String>>{

        @Override
        protected void onPostExecute(ArrayList<String> urlArray) {
            super.onPostExecute(urlArray);
            Log.d("demo", "urlArray : " + urlArray.toString());
            if(urlArray.size() <= 0){
                Toast.makeText(MainActivity.this, "No URLs Found",Toast.LENGTH_LONG).show();
                prevImage.setEnabled(false);
                nextImage.setEnabled(false);
                textView.setText("");
                selectedImage.setImageDrawable(getDrawable(R.drawable.tranparent));
            }else {
                if(urlArray.size() > 1){
                    prevImage.setEnabled(true);
                    nextImage.setEnabled(true);
                }
                imgIndex = 0;
                new GetImages(selectedImage).execute(urlArray.get(0));
            }
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            URL url;

            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        //String[] str = line.split("\n");
                        //Log.d("demo", "URLs : " + line);
                        //urlArray = new ArrayList<String>(Arrays.asList(str));
                        urlArray.add(line);
                    }
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return urlArray;
        }
    }

    class GetImages extends AsyncTask<String, Void, Void>{

        ImageView imageView;
        Bitmap bitmap = null;

        public GetImages(ImageView iv) {
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
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (bitmap != null && imageView != null) {
                imageView.setImageBitmap(bitmap);
                progressBar.setVisibility(View.INVISIBLE);
                prevImage.setEnabled(true);
                nextImage.setEnabled(true);
                goButton.setEnabled(true);
            }
        }

    }
}
