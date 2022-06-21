package com.example.stillhet.ui.music;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.stillhet.databinding.FragmentRecommendationsBinding;
import com.example.stillhet.Сlasses.Music;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecommendationsFragment extends Fragment {

    private FragmentRecommendationsBinding binding;

    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    ProgressBar progressBar;

    Boolean checkIn = false;
    JcPlayerView jcPlayerView;
    TextView playerButton;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    private  int currentIndex;

    ArrayList<MusicState> states;
    private ArrayList<String> songName, artist, time, link;

    DatabaseReference musicDB = FirebaseDatabase.getInstance().getReference("Music");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecommendationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        states = new ArrayList<>();
        songName = new ArrayList<>();
        artist = new ArrayList<>();
        time = new ArrayList<>();
        link = new ArrayList<>();
        progressBar = view.findViewById(R.id.progressBar4);
        jcPlayerView = requireActivity().findViewById(R.id.player);
        playerButton = requireActivity().findViewById(R.id.playerButton);

        recyclerView = view.findViewById(R.id.Recommendations_music);
        musicDB.addValueEventListener(getMusic);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    ValueEventListener getMusic = new ValueEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (states.size()>0 || songName.size()>0 || artist.size()>0 || time.size()>0 || link.size()>0)
            {
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
            final int min = 1;
            final int max = 10;
            final int rnd = random(min, max);

            for (int i = 0; i < songName.size(); i ++)
                if(states.size() < 26)
                    if(i % rnd == 0) {
                        states.add(new MusicState(songName.get(i), artist.get(i), time.get(i), link.get(i)));
                        jcAudios.add(JcAudio.createFromURL(songName.get(i), link.get(i)));
                    }

            if(getActivity() != null) {
                musicAdapter = new MusicAdapter(RecommendationsFragment.this.getContext(), states, stateClickListener);
                progressBar.setVisibility(View.GONE);
                musicAdapter.setSelectedPosition(0);
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

    MusicAdapter.OnStateClickListener stateClickListener = (state, position) -> {
        changeSelectedSong(position);
        jcPlayerView.playAudio(jcAudios.get(position));
        jcPlayerView.setVisibility(View.VISIBLE);
        playerButton.setVisibility(View.VISIBLE);
        jcPlayerView.createNotification(R.drawable.okno);
    };

    public static int random(int min, int max)
    {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    public void changeSelectedSong(int index) {
        musicAdapter.notifyItemChanged(musicAdapter.getSelectedPosition());
        currentIndex = index;
        musicAdapter.setSelectedPosition(currentIndex);
        musicAdapter.notifyItemChanged(currentIndex);
    }
}