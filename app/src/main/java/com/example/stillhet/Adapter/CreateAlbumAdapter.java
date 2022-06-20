package com.example.stillhet.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stillhet.R;
import com.example.stillhet.StatesForAdapter.MusicState;

import java.util.ArrayList;
import java.util.List;

public class CreateAlbumAdapter extends RecyclerView.Adapter<CreateAlbumAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<MusicState> adapter;
    public ArrayList<MusicState> selectedValues;

    public CreateAlbumAdapter(Context context, List<MusicState> adapter) {
        this.inflater = LayoutInflater.from(context);
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public CreateAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.album_songs_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CreateAlbumAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MusicState state = adapter.get(position);
        holder.TitleView.setText(state.getSongName());
        holder.ArtistView.setText(state.getArtist());
        holder.TimeView.setText(state.getTime());
        holder.LinkView.setText(state.getLink());
        holder.checkBox.setOnClickListener(v -> {
            if(holder.checkBox.isChecked())
                selectedValues.add(state);
            else
                selectedValues.remove(state);
        });
    }

    @Override
    public int getItemCount() {
        return adapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView TitleView, ArtistView, TimeView, LinkView;
        CheckBox checkBox;
        ViewHolder(View view){
            super(view);
            TitleView = view.findViewById(R.id.new_title);
            ArtistView = view.findViewById(R.id.new_artist);
            TimeView = view.findViewById(R.id.new_duration);
            LinkView = view.findViewById(R.id.new_link);
            checkBox = view.findViewById(R.id.new_checkBox);
            selectedValues = new ArrayList<>();
        }
    }

    public ArrayList<MusicState> listOfSelectedActivities(){
        return selectedValues;
    }

}
