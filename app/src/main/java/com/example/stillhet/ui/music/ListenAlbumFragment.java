package com.example.stillhet.ui.music;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.stillhet.Adapter.MusicAdapter;
import com.example.stillhet.R;
import com.example.stillhet.StatesForAdapter.MusicState;
import com.example.stillhet.databinding.FragmentListenAlbumBinding;
import com.example.stillhet.Сlasses.AlbumMusic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListenAlbumFragment extends Fragment {

    FragmentListenAlbumBinding binding;

    EditText name, creator;
    RecyclerView recyclerView;

    Boolean checkIn = false;
    JcPlayerView jcPlayerView;
    TextView playerButton;
    ImageView imageView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    private  int currentIndex;

    ArrayList<MusicState> states;
    MusicAdapter musicAdapter;
    private ArrayList<String> SongName, Artist, Time, Link;

    DatabaseReference allMusicDB;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListenAlbumBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = view.findViewById(R.id.listen_album_reView);
        jcPlayerView = requireActivity().findViewById(R.id.player);
        playerButton = requireActivity().findViewById(R.id.playerButton);
        imageView = view.findViewById(R.id.imageView3);
        SongName = new ArrayList<>();
        Artist = new ArrayList<>();
        Time = new ArrayList<>();
        Link = new ArrayList<>();
        states = new ArrayList<>();

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            String albumName = bundle.getString("albumName");
            String description = bundle.getString("description");
            String creatorName = bundle.getString("creator");
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://stillhet-a0d4f.appspot.com/AlbumImages").child(albumName + description);
            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            });

            name = view.findViewById(R.id.albumName);
            creator = view.findViewById(R.id.albumDescription);

            name.setText(albumName);
            creator.setText(description);

            String child = description+albumName+creatorName;
            allMusicDB = FirebaseDatabase.getInstance().getReference("Albums").child(child).child("Songs");
            allMusicDB.addValueEventListener(getMusicData);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        allMusicDB.removeEventListener(getMusicData);
        binding = null;
    }

    ValueEventListener getMusicData = new ValueEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (states.size()>0 || SongName.size()>0 || Artist.size()>0 || Time.size()>0 || Link.size()>0)
            {
                states.clear();
                SongName.clear();
                Artist.clear();
                Time.clear();
                Link.clear();
            }
            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                AlbumMusic music = dataSnapshot.getValue(AlbumMusic.class);
                assert music != null;
                SongName.add(music.songName);
                Artist.add(music.artist);
                Time.add(music.time);
                Link.add(music.link);
                checkIn = true;
                currentIndex = 0;
                jcAudios.add(JcAudio.createFromURL(music.songName, music.link));
            }
            for (int i = 0; i < SongName.size(); i ++)
                states.add(new MusicState(SongName.get(i), Artist.get(i), Time.get(i), Link.get(i)));

            musicAdapter = new MusicAdapter(ListenAlbumFragment.this.getContext(), states, stateClickListener);
            musicAdapter.setSelectedPosition(0);
            recyclerView.setAdapter(musicAdapter);

            if(checkIn) {
                jcPlayerView.initPlaylist(jcAudios, null);
            }
            else {
                Toast.makeText(getActivity(), "Пустой плейлист", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w("LogFragment", "loadLog:onCancelled", error.toException());
        }
    };

    MusicAdapter.OnStateClickListener stateClickListener = (state, position) -> {
        changeSelectedSong(position);
        jcPlayerView.playAudio(jcAudios.get(position));
        jcPlayerView.setVisibility(View.VISIBLE);
        playerButton.setVisibility(View.VISIBLE);
        jcPlayerView.createNotification(R.drawable.okno);
    };

    public void changeSelectedSong(int index) {
        musicAdapter.notifyItemChanged(musicAdapter.getSelectedPosition());
        currentIndex = index;
        musicAdapter.setSelectedPosition(currentIndex);
        musicAdapter.notifyItemChanged(currentIndex);
    }
}