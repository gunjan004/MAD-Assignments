package com.example.inclass12;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>  {


    ArrayList<TripDetails> trip_list;
    ITask iTask;

    public MyRecyclerViewAdapter(ArrayList<TripDetails> trip_list, ITask iTask)
    {
        this.trip_list=trip_list;
        this.iTask=iTask;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rec_list,viewGroup,false);


        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final TripDetails tripDetails=trip_list.get(i);
        viewHolder.txtTripName.setText(tripDetails.TripName);
        viewHolder.txtUsername.setText(tripDetails.userName);
        viewHolder.txtDest.setText(tripDetails.destinationCity);
        viewHolder.txtdate.setText(tripDetails.TripDate);

        viewHolder.txtTripName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iTask.onrecycleClick(tripDetails);
            }
        });

        viewHolder.txtUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iTask.onrecycleClick(tripDetails);
            }
        });


        viewHolder.txtDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iTask.onrecycleClick(tripDetails);
            }
        });
        viewHolder.txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iTask.onrecycleClick(tripDetails);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trip_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTripName,txtDest,txtdate,txtUsername;
        //Email email;
        public ViewHolder(View itemView){
            super(itemView);

            txtdate=(TextView)itemView.findViewById(R.id.rec_date);
            txtDest=(TextView)itemView.findViewById(R.id.rec_DestCity);
            txtUsername=(TextView)itemView.findViewById(R.id.rec_userName);
            txtTripName=(TextView)itemView.findViewById(R.id.rec_tipName);




        }





    }

}
