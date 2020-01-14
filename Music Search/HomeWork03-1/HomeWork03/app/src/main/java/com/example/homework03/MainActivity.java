package com.example.homework03;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private SeekBar seek;
    public int seekData = 10;
    private TextView limitTextView;
    private Button btnReset;
    private Button btnSearch;
    private ListView myListView;
    private EditText edSearchKey;
    private ProgressDialog progressDialog;
    private MusicAdapter musicAdapter;
    private Switch switchPriceDate;
    static String MUSIC_KEY = "MyMusic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("iTunes Music Search");

        btnReset = findViewById(R.id.btnReset);
        btnSearch = findViewById(R.id.btnSearch);
        seek = findViewById(R.id.seekBarLim);
        limitTextView = findViewById(R.id.tvLimitSet);
        limitTextView.setText(String.valueOf(10));
        myListView = findViewById(R.id.listViewId);
        edSearchKey = findViewById(R.id.etSearchKey);
        switchPriceDate = findViewById(R.id.switchPriceDate);
        btnReset = findViewById(R.id.btnReset);

        //ON = Date, OFF = Price
        switchPriceDate.setChecked(true);
        switchPriceDate.setTextOn("Date");
        switchPriceDate.setTextOff("Price");

        Log.d("demo", "ON: " + switchPriceDate.getTextOn().toString());
        Log.d("demo", "OFF: " + switchPriceDate.getTextOff().toString());

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                seekData = progress + 10;
                limitTextView.setText(String.valueOf(seekData));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isConnected()) {
                    if (edSearchKey.getText().toString().trim().length() == 0) {
                        Toast.makeText(MainActivity.this, "Enter a search key.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("demo", edSearchKey.getText().toString());
                        Log.d("demo", limitTextView.getText().toString());

                        //Start Async Task after adding parameters to the URL
                        RequestParams params = new RequestParams();
                        params.addParameter("term", edSearchKey.getText().toString())
                                .addParameter("limit", limitTextView.getText().toString());

                        new GetJsonDataUsingGetParams(params).execute("https://itunes.apple.com/search");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
                }

                myListView.setVisibility(ListView.VISIBLE);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset everything
                seek.setProgress(0);
                myListView.setVisibility(View.INVISIBLE);
                switchPriceDate.setChecked(true);
                edSearchKey.setText("");
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    private class GetJsonDataUsingGetParams extends AsyncTask<String, Void, ArrayList<Music>> {

        RequestParams mParams;

        public GetJsonDataUsingGetParams(RequestParams params){
            mParams = params;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching Music...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected ArrayList<Music> doInBackground(String... strings) {

            HttpURLConnection conn = null;
            BufferedReader br = null;
            String json = null;
            ArrayList<Music> result = new ArrayList<>();

            try {

                URL url = new URL(mParams.getEncodedUrl(strings[0]));
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){

                    json = IOUtils.toString(conn.getInputStream(),"UTF8");

                    JSONObject root =new JSONObject(json);
                    JSONArray musicArray = root.getJSONArray("results");

                    for (int i = 0; i < musicArray.length(); i++) {

                        JSONObject musicJson = musicArray.getJSONObject(i);
                        Music music = new Music();
                        music.titleTrack = musicJson.getString("trackName");
                        music.artistName = musicJson.getString("artistName");
                        music.trackPrice = musicJson.getString("trackPrice");

                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");
                        String dt = musicJson.getString("releaseDate");
                        Date myDate = formatter.parse(dt);
                        SimpleDateFormat newFormat = new SimpleDateFormat("MM-DD-YYYY");
                        String finalString = newFormat.format(myDate);

                        music.date = finalString;

                        music.trackImage = musicJson.getString("artworkUrl100");
                        music.albumName = musicJson.getString("collectionName");
                        music.albumPrice = musicJson.getString("collectionPrice");
                        music.genre = musicJson.getString("primaryGenreName");

                        result.add(music);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(final ArrayList<Music> res) {

            progressDialog.dismiss();

            if(res.size() > 0){
                Log.d("demo", res.toString());

                if(!switchPriceDate.isChecked()){
                    //sort by price

                    Collections.sort(res, new Comparator<Music>() {
                        @Override
                        public int compare(Music music1, Music music2) {
                            return extractInt(music1.trackPrice) - extractInt(music2.trackPrice);
                        }

                        int extractInt(String s) {
                            String num = s.replaceAll("\\D", "");
                            return num.isEmpty() ? 0 : Integer.parseInt(num);
                        }
                    });

                    Log.d("demo", res.toString());

                } else if(switchPriceDate.isChecked()){
                    //sort by date - ascending

                    Collections.sort(res, new Comparator<Music>() {

                        DateFormat f = new SimpleDateFormat("MM-DD-YYYY");

                        @Override
                        public int compare(Music music1, Music music2) {
                            //return music1.trackPrice.compareTo(music2.trackPrice);
                            try {
                                return f.parse(music1.date).compareTo(f.parse(music2.date));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });
                }

                musicAdapter = new MusicAdapter(MainActivity.this, R.layout.list_item_view, res);
                myListView.setAdapter(musicAdapter);
                myListView.setVisibility(View.VISIBLE);

                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("demo", "Clicked: " + res.get(position));

                        Intent newIntent = new Intent(MainActivity.this, DisplayActivity.class);
                        newIntent.putExtra(MUSIC_KEY, res.get(position));
                        startActivity(newIntent);
                    }
                });

            } else{
                Log.d("demo", "No data received");
            }
        }

    }

}
