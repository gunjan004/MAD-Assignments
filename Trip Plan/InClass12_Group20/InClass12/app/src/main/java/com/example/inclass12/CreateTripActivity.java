package com.example.inclass12;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreateTripActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    OkHttpClient client;
    String userId;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    DatabaseReference mroot;
    String destinationCity;
    String tripDate;
    EditText tripName;
    String userName;
    ImageButton logout;

    HashMap<Integer, PlacesData> user_res_map;
    List<PlacesData> restaurents_list;
    List<PlacesData> user_restaurents_list;
    Button btnCreate;
    Button btnView;
    private GoogleMap mMap;
    private static final String TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;

    private TextView mUserName;
    private DatePickerDialog mPickerDialogue;
    private ImageView imageView;
    private TextView mSelectedDate;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Create a Trip");
        database = FirebaseDatabase.getInstance();
        mroot = database.getReference();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        client = new OkHttpClient();
        logout = findViewById(R.id.imgLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
            }
        });

        user_res_map = new HashMap<>();
        tripName = findViewById(R.id.edit_tripname);
        btnCreate = findViewById(R.id.btnCreate);
        btnView = findViewById(R.id.btnView);
        restaurents_list = new ArrayList<>();
        user_restaurents_list = new ArrayList<>();
        imageView = findViewById(R.id.imageCalender);
        mSelectedDate = findViewById(R.id.selectedDate);
        mUserName = findViewById(R.id.txtUserName);
        mAutocompleteTextView = findViewById(R.id.autoCompleteCity);
        mAutocompleteTextView.setThreshold(3);

        if(getIntent() != null && getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("Name");
            userId = getIntent().getExtras().getString("Id");
            mUserName.setText(userName);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int day= calendar.get(Calendar.DAY_OF_MONTH);
                int month= calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                mPickerDialogue = new DatePickerDialog(CreateTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tripDate=dayOfMonth + "/" + (month+1) + "/"+year;
                        mSelectedDate.setText(dayOfMonth + "/" + (month+1) + "/"+year);
                        mSelectedDate.setVisibility(View.VISIBLE);
                    }
                },day,month,year);
                mPickerDialogue.updateDate(year,month,day);
                mPickerDialogue.show();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTrip();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CreateTripActivity.this, ViewTripsActivity.class));
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(CreateTripActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);


    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            LatLng latLng=place.getLatLng();
            destinationCity=place.getName().toString();

            mMap.clear();
            fetchRestaurents(latLng);
        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();


        mMap.setOnMarkerClickListener(this);

    }

    public void fetchRestaurents(LatLng latLng)
    {

        mMap.clear();
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latLng.latitude+","+latLng.longitude+"&radius=1600&type=restaurant&key=AIzaSyAraRWuZyLViRnXaARsoV2qFAoWkfPNQWs";
        final Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latLng.latitude+","+latLng.longitude+"&radius=1600&type=restaurant&key=AIzaSyAraRWuZyLViRnXaARsoV2qFAoWkfPNQWs")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful())
                {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    JSONObject root = null;
                    try {
                        root = new JSONObject(response.body().string().toString());

                        restaurents_list.clear();
                        JSONArray msg_arr=root.getJSONArray("results");
                        for(int i=0;i<msg_arr.length();i++)
                        {

                            JSONObject msg_JSONobj=msg_arr.getJSONObject(i);

                            PlacesData restaurents=new PlacesData();
                            restaurents.name=msg_JSONobj.getString("name");
                            restaurents.id=msg_JSONobj.getString("id");
                            restaurents.placeId=msg_JSONobj.getString("place_id");

                            JSONObject location=msg_JSONobj.getJSONObject("geometry").getJSONObject("location");
                            double latitude = Double.parseDouble(location.getString("lat"));
                            double longitude = Double.parseDouble(location.getString("lng"));
                            LatLng latLng1 = new LatLng(latitude,longitude);
                            restaurents.latitude=latitude;
                            restaurents.longitude=longitude;

                            restaurents_list.add(restaurents);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            addMarkers();
                        }
                    });

                }
            }
        });
    }
    public void  addMarkers()
    {

        mMap.clear();

        if(restaurents_list.size()!=0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < restaurents_list.size(); i++) {
                PlacesData restaurents = restaurents_list.get(i);

                LatLng latLng = new LatLng(restaurents.latitude, restaurents.longitude);
                builder.include(latLng);


                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(restaurents.name)
                        .snippet(restaurents.placeId)

                );
            }

            LatLngBounds bounds = builder.build();
            ;

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.10);


            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            //mMap.animateCamera(cu);
            mMap.animateCamera(cu);
        }
        else
        {
            Toast.makeText(this, "No restraunts found nearby", Toast.LENGTH_SHORT).show();
        }

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        LatLng latLng=marker.getPosition();
        String Resto_name = marker.getTitle();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                CreateTripActivity.this);

        // set title
        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to add this restaurent")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                LatLng rest_latLong=marker.getPosition();
                                String res_name=marker.getTitle();
                                PlacesData restaurents=new PlacesData();
                                restaurents.name=res_name;
                                restaurents.latitude=rest_latLong.latitude;
                                restaurents.longitude=rest_latLong.longitude;
                                restaurents.placeId=marker.getSnippet();

                                user_restaurents_list.add(restaurents);

                            }
                        }
                )
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        LatLng rest_latLong=marker.getPosition();
                        String res_name=marker.getTitle();
                        PlacesData restaurents=new PlacesData();
                        restaurents.name=res_name;
                        restaurents.latitude=rest_latLong.latitude;
                        restaurents.longitude=rest_latLong.longitude;
                        restaurents.placeId=marker.getSnippet();
                        for(int i=0;i<user_restaurents_list.size();i++)
                        {
                            PlacesData res=user_restaurents_list.get(i);
                            if(restaurents.placeId.equals(res.placeId))
                            {
                                user_restaurents_list.remove(i);
                            }
                        }
                        /*
                        if(user_restaurents_list.contains(restaurents))
                        {
                            user_restaurents_list.remove(restaurents);
                        }
                        */
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker());

                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        for(PlacesData r:user_restaurents_list)
        {
            Log.d("res",r.toString());
        }

        return false;
    }

    public void createTrip()
    {
        EditText tripName = findViewById(R.id.edit_tripname);
        TextView tripdate = findViewById(R.id.selectedDate);
        if(tripName.getText().length()<=0)
        {
            Toast.makeText(this, "Please enter Trip Name", Toast.LENGTH_SHORT).show();
        }
        else if(mAutocompleteTextView.getText().length()<=0)
        {
            Toast.makeText(this, "Please enter Destination City", Toast.LENGTH_SHORT).show();
        }
        else if(tripdate.getText().toString().equals("selectedDate"))
        {
            Toast.makeText(this, "Please select Trip date", Toast.LENGTH_SHORT).show();
        }
        else if(user_restaurents_list.size()==0)
        {
            Toast.makeText(this, "Please select at least one restaurent you want to visit", Toast.LENGTH_SHORT).show();
        }
        else if(user_restaurents_list.size()>15)
        {
            Toast.makeText(this, "Please select at max 15 restaurents", Toast.LENGTH_SHORT).show();
        }
        else {
            String tripId = UUID.randomUUID().toString();
            DatabaseReference users = mroot.child("Trips/" + tripId);
            TripDetails trip = new TripDetails();
            trip.destinationCity = destinationCity;
            trip.userRestaurents = user_restaurents_list;
            trip.TripDate = tripDate;
            trip.TripName = tripName.getText().toString();
            trip.tripId = tripId;
            trip.userName = userName;
            users.setValue(trip);
            Toast.makeText(this, "Trip successfully created", Toast.LENGTH_SHORT).show();
        }
    }
}
