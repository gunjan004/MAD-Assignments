package com.example.homework03;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<Music> {

    public MusicAdapter(@NonNull Context context, int resource, @NonNull List<Music> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Music music = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvTitleTrackData);
        TextView tvPrice = convertView.findViewById(R.id.tvTrackPriceData);
        TextView tvArtist = convertView.findViewById(R.id.tvArtistNameData);
        TextView tvDate = convertView.findViewById(R.id.tvDateData);

        tvTitle.setText(music.titleTrack);
        tvPrice.setText(music.trackPrice);
        tvArtist.setText(music.artistName);
        tvDate.setText(music.date);

        return convertView;
    }
}
