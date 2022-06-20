package com.example.stillhet.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stillhet.R;
import com.example.stillhet.StatesForAdapter.AlbumState;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<AlbumState> adapter;
    private final OnStateClickListener onStateClickListener;

    public AlbumAdapter(Context context, ArrayList<AlbumState> adapter, OnStateClickListener onClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.adapter = adapter;
        this.onStateClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.album_row, parent, false);
        return new ViewHolder(view);
    }

    public interface OnStateClickListener {
        void onStateClick(AlbumState state, int position);
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.ViewHolder holder, int position) {
        AlbumState state = adapter.get(position);

        holder.AlbumName.setText(state.getAlbumName());
        holder.Creator.setText(state.getCreator());
        holder.Description.setText(state.getDescription());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://stillhet-a0d4f.appspot.com/AlbumImages").child(state.getImage());
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.Image.setImageBitmap(bitmap);
        });


        holder.itemView.setOnClickListener(v -> onStateClickListener.onStateClick(state, position));
    }

    @Override
    public int getItemCount() { return adapter.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView AlbumName, Creator, Description;
        final ImageView Image;
        ViewHolder(View view) {
            super(view);
            AlbumName = view.findViewById(R.id.album_name);
            Creator = view.findViewById(R.id.album_creator_name);
            Description = view.findViewById(R.id.album_description);
            Image = view.findViewById(R.id.imageView2);
        }
    }
}
