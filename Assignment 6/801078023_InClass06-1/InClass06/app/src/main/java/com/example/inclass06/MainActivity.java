package com.example.inclass06;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button goButton;
    private ArrayList<String> keywords;
    private String selectedKeyword;
    private AlertDialog.Builder builder;
    private TextView textViewSearch;
    private int imgIndex;
    private Bitmap DisplayImage;
    private ImageView selectedImage;
    private ImageView prevImage;
    private ImageView nextImage;
    private ProgressBar progressBar;
    private  ArrayList<Articles> result = new ArrayList<>();
    private TextView textViewTitle;
    private TextView textViewDesc;
    private TextView textViewPublished;
    private TextView textViewPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        goButton = findViewById(R.id.go_btn);
        textViewSearch = findViewById(R.id.textView_searchID);
        textViewTitle = findViewById(R.id.textView_Title);
        textViewDesc = findViewById(R.id.textView_Desc);
        textViewPublished = findViewById(R.id.textView_Published);
        textViewPage = findViewById(R.id.textView_PageCount);
        selectedImage = findViewById(R.id.imageView_MainID);
        prevImage = findViewById(R.id.imageView_Prev);
        nextImage = findViewById(R.id.imageView_Next);
        progressBar = findViewById(R.id.progressBar);
        prevImage.setEnabled(false);
        nextImage.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] options = {"Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"};

                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose a Keyword");
                builder.setCancelable(false);

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Log.d("demo", "onClick" + options[item]);
                        textViewSearch.setText(options[item]);
                        selectedKeyword = (String) options[item];
                        progressBar.setVisibility(View.VISIBLE);
                        goButton.setEnabled(false);

                        if (isConnected()) {
                            Toast.makeText(MainActivity.this, "Internet Connected", Toast.LENGTH_LONG).show();
                            imgIndex = 0;
                            new GetURL().execute("https://newsapi.org/v2/top-headlines?apiKey=4eb5241471654fbc83bc33ad84ce22bf&category=" + selectedKeyword);
                        } else {
                            Toast.makeText(MainActivity.this, "No Internet Please Check Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.show();
            }
        });

        prevImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp;
                if(imgIndex == 0) {
                    temp = result.size() - 1;
                    imgIndex = temp;
                }else{
                    temp = imgIndex - 1;
                    imgIndex = temp;
                }

                textViewTitle.setText(result.get(temp).getTitle()  == "null" ? "" : result.get(temp).getTitle());
                textViewDesc.setText(result.get(temp).getDescription() == "null" ? "" : result.get(temp).getDescription());
                textViewPublished.setText(result.get(temp).getPublishedAt()  == "null" ? "" : result.get(temp).getPublishedAt());
                int size = result.size() - 1;
                textViewPage.setText("Page " + temp + " of " + size);

                progressBar.setVisibility(View.VISIBLE);
                prevImage.setEnabled(false);
                nextImage.setEnabled(false);

                if(result.get(temp).getUrlToImage() != "null") {
                    new GetImages(selectedImage).execute(result.get(imgIndex).getUrlToImage());
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    selectedImage.setImageDrawable(getDrawable(R.drawable.tranparent));
                    prevImage.setEnabled(true);
                    nextImage.setEnabled(true);
                }
            }
        });

        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp;
                Log.d("demo", "image index old : " + imgIndex);

                if(imgIndex < result.size()-1){
                    temp = imgIndex + 1;
                    imgIndex = temp;
                    Log.d("demo", "temp: " + temp);
                    Log.d("demo", "image index new: " + imgIndex);

                    textViewTitle.setText(result.get(temp).getTitle()  == "null" ? "" : result.get(temp).getTitle());
                    textViewDesc.setText(result.get(temp).getDescription() == "null" ? "" : result.get(temp).getDescription());
                    textViewPublished.setText(result.get(temp).getPublishedAt()  == "null" ? "" : result.get(temp).getPublishedAt());
                    int size = result.size() -1;
                    textViewPage.setText("Page " + temp + " of " + size);

                    progressBar.setVisibility(View.VISIBLE);
                    prevImage.setEnabled(false);
                    nextImage.setEnabled(false);

                    if(result.get(temp).getUrlToImage() != "null") {
                      new GetImages(selectedImage).execute(result.get(imgIndex).getUrlToImage());
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        selectedImage.setImageDrawable(getDrawable(R.drawable.tranparent));
                        prevImage.setEnabled(true);
                        nextImage.setEnabled(true);
                    }
                }
                else{
                    temp = 0;
                    imgIndex = temp;
                    progressBar.setVisibility(View.VISIBLE);
                    prevImage.setEnabled(false);
                    nextImage.setEnabled(false);
                    new GetImages(selectedImage).execute(result.get(imgIndex).getUrlToImage());
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


    class GetURL extends AsyncTask<String, Void, ArrayList<Articles>> {

        @Override
        protected void onPostExecute(ArrayList<Articles> result) {

            super.onPostExecute(result);

            if (result.size() > 0) {
                Log.d("demo", result.toString());

                textViewTitle.setText(result.get(imgIndex).getTitle()  == "null" ? "" : result.get(imgIndex).getTitle());
                textViewDesc.setText(result.get(imgIndex).getDescription() == "null" ? "" : result.get(imgIndex).getDescription());
                textViewPublished.setText(result.get(imgIndex).getPublishedAt()  == "null" ? "" : result.get(imgIndex).getPublishedAt());
                int size = result.size() - 1;
                textViewPage.setText("Page " + imgIndex + " of " + size);

                prevImage.setEnabled(true);
                nextImage.setEnabled(true);
            } else {
                Log.d("demo", "Result is null");

                Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_LONG).show();
                prevImage.setEnabled(false);
                nextImage.setEnabled(false);
                textViewSearch.setText("");
                selectedImage.setImageDrawable(getDrawable(R.drawable.tranparent));
            }
            imgIndex = 0;
            new GetImages(selectedImage).execute(result.get(0).getUrlToImage());
        }

        @Override
        protected ArrayList<Articles> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            URL url;
            result = new ArrayList<>();
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray articles = root.getJSONArray("articles");

                    for (int i = 0; i < articles.length(); i++) {
                        JSONObject articleJson = articles.getJSONObject(i);
                        Articles article = new Articles();

                        article.setTitle(articleJson.getString("title"));
                        article.setUrlToImage(articleJson.getString("urlToImage"));
                        article.setPublishedAt(articleJson.getString("publishedAt"));
                        article.setDescription(articleJson.getString("description"));

                        result.add(article);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
    }

    class GetImages extends AsyncTask<String, Void, Void> {

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
            } else{
                selectedImage.setImageDrawable(getDrawable(R.drawable.tranparent));
               // selectedImage.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                prevImage.setEnabled(true);
                nextImage.setEnabled(true);
                goButton.setEnabled(true);
            }
        }

    }
}
