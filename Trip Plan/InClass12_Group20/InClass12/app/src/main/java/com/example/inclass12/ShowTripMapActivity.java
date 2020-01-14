package com.example.inclass12;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class ShowTripMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TripDetails tripDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trip_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.setTitle("Trip Map");
        if(getIntent()!=null && getIntent().getExtras()!=null)
        {
            tripDetails= (TripDetails) getIntent().getExtras().getSerializable("trip");
        }
    }

    @Override
    public void onBackPressed() {
        mMap.clear();
        super.onBackPressed();
    }

    public void  addMarkers()
    {

        mMap.clear();
        List<PlacesData> restaurents_list=tripDetails.userRestaurents;
        TextView txt = findViewById(R.id.TripCityshow);
        txt.setText(tripDetails.destinationCity.toString());
        LatLngBounds.Builder builder=new LatLngBounds.Builder();

        for(int i=0;i<restaurents_list.size();i++) {
            PlacesData restaurents=restaurents_list.get(i);

            LatLng latLng=new LatLng(restaurents.latitude, restaurents.longitude);
            builder.include(latLng);


            mMap.addMarker(new MarkerOptions().position(latLng)
                    .title(restaurents.name)
                    .snippet(restaurents.placeId)

            );
        }

        LatLngBounds bounds=builder.build();;

        int width=getResources().getDisplayMetrics().widthPixels;
        int height=getResources().getDisplayMetrics().heightPixels;
        int padding=(int)(width * 0.10);


        CameraUpdate cu= CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);

        mMap.animateCamera(cu);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        addMarkers();

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

}
