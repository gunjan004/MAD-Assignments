package com.group20.pathsactivity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> listPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listPoints = new ArrayList<>();
        String jsonData = loadJSONFromAsset(this);
        try {
            JSONObject root = new JSONObject(jsonData.toString());
            JSONArray array = root.getJSONArray("points");
            for (int i = 0; i <= array.length(); i++) {
                JSONObject pointJSON = array.getJSONObject(i);
                String lat = pointJSON.getString("latitude");
                String lng = pointJSON.getString("longitude");
                double latitude = Double.parseDouble(lat);
                double longitude = Double.parseDouble(lng);
                LatLng point = new LatLng(latitude, longitude);
                listPoints.add(point);
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng start=listPoints.get(0);
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(start).title("Start Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        LatLng end=listPoints.get(listPoints.size()-1);

        mMap.addMarker(new MarkerOptions().position(end).title("End Location"));
        Polyline polyline;
        polyline = googleMap.addPolyline(new PolylineOptions().clickable(true).addAll(listPoints).width(5).color(Color.BLUE));

        LatLngBounds.Builder builder=new LatLngBounds.Builder();
        for(int i=0;i<listPoints.size()-1;i++)
        {
            builder.include(listPoints.get(i));
        }

        LatLngBounds bounds=builder.build();;
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int)(width * 0.10);

        CameraUpdate cu=CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);
        mMap.animateCamera(cu);
    }
    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("trip.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
