package com.example.firebasedb;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;



public class ArtistList extends ArrayAdapter<Artist> {

    public Activity context;
    private List<Artist> artistList;

    public ArtistList(Activity context, List<Artist> artistList){
        super(context,R.layout.list_layout, artistList);
        this.context = context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,@NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout,parent, false);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);

        Artist artist = artistList.get(position);

        textViewName.setText(artist.getArtistName());

        return listViewItem;

    }
}
