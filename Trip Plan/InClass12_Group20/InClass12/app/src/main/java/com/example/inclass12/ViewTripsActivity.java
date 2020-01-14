package com.example.inclass12;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewTripsActivity extends AppCompatActivity implements ITask {

    private ArrayList<TripDetails> trip_list;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rec_adapter;
    private RecyclerView.LayoutManager rec_layout;
    FirebaseDatabase database;
    DatabaseReference mroot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trips);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("View Trip");

        database = FirebaseDatabase.getInstance();
        mroot = database.getReference();
        trip_list=new ArrayList<>();

        fetchlist();
    }

    @Override
    public void onrecycleClick(TripDetails tripDetails) {

        Intent intent= new Intent(ViewTripsActivity.this, ShowTripMapActivity.class);

        intent.putExtra("trip", tripDetails);

        startActivity(intent);
    }


    public void fetchlist()
    {

        DatabaseReference users = mroot.child("Trips");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    for(DataSnapshot snap: dataSnapshot.getChildren())
                    {
                        if(snap!=null)
                        {
                            TripDetails tripDetails=snap.getValue(TripDetails.class);
                            trip_list.add(tripDetails);
                            //setList();
                        }
                    }

                    setList();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public  void setList()
    {
        recyclerView = findViewById(R.id.trip_recycler);
        recyclerView.setHasFixedSize(true);
        rec_layout=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rec_layout);

        rec_adapter=new MyRecyclerViewAdapter(trip_list,this);
        recyclerView.setAdapter(rec_adapter);

    }
}
