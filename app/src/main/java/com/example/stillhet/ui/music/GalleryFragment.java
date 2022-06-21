package com.example.stillhet.ui.music;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.example.stillhet.databinding.FragmentGalleryBinding;
import com.example.stillhet.Сlasses.Music;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    RecyclerView recyclerView;
    EditText findLine;
    ArrayList<MusicState> states;
    MusicAdapter musicAdapter;
    ProgressBar progressBar;

    Boolean checkIn = false;
    JcPlayerView jcPlayerView;
    TextView playerButton;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();

    FirebaseAuth musicAuth = FirebaseAuth.getInstance();
    FirebaseUser user = musicAuth.getCurrentUser();
    DatabaseReference myMusicDB  = FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(user.getDisplayName()));
    DatabaseReference allMusicDB = FirebaseDatabase.getInstance().getReference("Music");
    private  int currentIndex;

    private ArrayList<String> songName, artist, time, link;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.My_music);
        jcPlayerView = requireActivity().findViewById(R.id.player);
        playerButton = requireActivity().findViewById(R.id.playerButton);
        songName = new ArrayList<>();
        artist = new ArrayList<>();
        time = new ArrayList<>();
        link = new ArrayList<>();
        states = new ArrayList<>();
        progressBar = root.findViewById(R.id.progressBar3);

        myMusicDB.addValueEventListener(getMusicData);

        findLine = root.findViewById(R.id.findLine2);
        findLine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(findLine.getText().toString().equals("")) {
                    allMusicDB.removeEventListener(getMusicData);
                    recyclerView.removeAllViews();
                    myMusicDB.addValueEventListener(getMusicData);
                }
                else {
                    myMusicDB.removeEventListener(getMusicData);
                    recyclerView.removeAllViews();
                    allMusicDB.addValueEventListener(getMusicDataOnTextChanged);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    ValueEventListener getMusicData = new ValueEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (states.size()>0 || songName.size()>0 || artist.size()>0 || time.size()>0 || link.size()>0)
            {
                jcAudios.clear();
                states.clear();
                songName.clear();
                artist.clear();
                time.clear();
                link.clear();
            }
            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                Music music = dataSnapshot.getValue(Music.class);
                assert music != null;
                songName.add(music.SongName);
                artist.add(music.Artist);
                time.add(music.Time);
                link.add(music.Link);
                checkIn = true;
                currentIndex = 0;
                jcAudios.add(JcAudio.createFromURL(music.SongName, music.Link));
            }
            for (int i = 0; i < songName.size(); i ++)
                states.add(new MusicState(songName.get(i), artist.get(i), time.get(i), link.get(i)));

            if (getActivity() != null) {
                musicAdapter = new MusicAdapter(GalleryFragment.this.getContext(), states, stateClickListener);
                musicAdapter.setSelectedPosition(0);
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(musicAdapter);
            }

            if(checkIn) {
                jcPlayerView.initPlaylist(jcAudios, null);
            }
            else {
                Toast.makeText(getActivity(), "Пустой плейлист", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            progressBar.setVisibility(View.GONE);
        }
    };

    ValueEventListener getMusicDataOnTextChanged = new ValueEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (states.size()>0 || songName.size()>0 || artist.size()>0 || time.size()>0 || link.size()>0)
            {
                jcAudios.clear();
                states.clear();
                songName.clear();
                artist.clear();
                time.clear();
                link.clear();
            }
            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                Music music = dataSnapshot.getValue(Music.class);
                assert music != null;
                songName.add(music.SongName);
                artist.add(music.Artist);
                time.add(music.Time);
                link.add(music.Link);
                checkIn = true;
                currentIndex = 0;
            }
            for (int i = 0; i < songName.size(); i ++)
                if(songName.get(i).toLowerCase().contains(findLine.getText()) || artist.get(i).toLowerCase().contains(findLine.getText()) ||
                        songName.get(i).toUpperCase().contains(findLine.getText()) || artist.get(i).toUpperCase().contains(findLine.getText()) ||
                        songName.get(i).contains(findLine.getText()) || artist.get(i).contains(findLine.getText())) {
                    states.add(new MusicState(songName.get(i), artist.get(i), time.get(i), link.get(i)));
                    jcAudios.add(JcAudio.createFromURL(songName.get(i), link.get(i)));
                }

            if (getActivity() != null) {
                musicAdapter = new MusicAdapter(GalleryFragment.this.getContext(), states, stateClickListener);
                musicAdapter.setSelectedPosition(-1);
                recyclerView.setAdapter(musicAdapter);
            }

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