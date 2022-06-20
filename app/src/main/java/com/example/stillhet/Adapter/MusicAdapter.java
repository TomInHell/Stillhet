package com.example.stillhet.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stillhet.R;
import com.example.stillhet.StatesForAdapter.MusicState;
import com.example.stillhet.Сlasses.Music;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private  int selectedPosition;
    private final LayoutInflater inflater;
    private final List<MusicState> adapter;
    private final OnStateClickListener onClickListener;

    public MusicAdapter(Context context, List<MusicState> adapter, OnStateClickListener onClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.adapter = adapter;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.songs_row, parent, false);
        return new ViewHolder(view);
    }

    public interface OnStateClickListener{
        void onStateClick(MusicState state, int position) throws IOException;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MusicState state = adapter.get(position);

        if(state != null) {
            if (selectedPosition == position) {
                holder.PlayActive.setVisibility(View.VISIBLE);
            }
            else {
                holder.PlayActive.setVisibility(View.GONE);
            }
        }

        holder.TitleView.setText(Objects.requireNonNull(state).getSongName());
        holder.ArtistView.setText(state.getArtist());
        holder.TimeView.setText(state.getTime());
        holder.LinkView.setText(state.getLink());

        holder.itemView.setOnClickListener(v -> {
            try {
                onClickListener.onStateClick(state, position);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return adapter.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        final ImageView PlayActive, addOrDelete;
        final TextView TitleView, ArtistView, TimeView, LinkView;

        ViewHolder(View view){
            super(view);
            TitleView = view.findViewById(R.id.tv_title);
            ArtistView = view.findViewById(R.id.tv_artist);
            TimeView = view.findViewById(R.id.duration);
            PlayActive = view.findViewById(R.id.iv_play_active);
            LinkView = view.findViewById(R.id.tv_link);
            addOrDelete = view.findViewById(R.id.add_or_delete);
            addOrDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.add_or_delete);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            FirebaseAuth musicAuth = FirebaseAuth.getInstance();
            FirebaseUser user = musicAuth.getCurrentUser();
            switch (item.getItemId()){
                case R.id.addInPlaylist:
                    DatabaseReference mDB = FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(Objects.requireNonNull(user).getDisplayName()));
                    Music music = new Music(TitleView.getText().toString(), ArtistView.getText().toString(), TimeView.getText().toString(), LinkView.getText().toString());
                    mDB.child(TitleView.getText().toString() + ArtistView.getText().toString() + TimeView.getText().toString()).setValue(music);
                    return true;

                case R.id.deleteFromPlaylist:
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference.child(Objects.requireNonNull(Objects.requireNonNull(user).getDisplayName())).orderByChild("Link").equalTo(LinkView.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1: snapshot.getChildren()) {
                                snapshot1.getRef().removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("onCancelled", String.valueOf(error.toException()));
                        }
                    });
                    return true;

                default:
                    return false;
            }
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
