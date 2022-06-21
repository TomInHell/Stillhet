package com.example.stillhet.ui.music;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stillhet.Adapter.AlbumAdapter;
import com.example.stillhet.R;
import com.example.stillhet.StatesForAdapter.AlbumState;
import com.example.stillhet.databinding.FragmentAlbumBinding;
import com.example.stillhet.Сlasses.Album;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlbumFragment extends Fragment {

    private FragmentAlbumBinding binding;

    TextView textView11;
    RecyclerView recyclerMyView, recyclerAllView;
    AlbumAdapter adapter, otherAdapter;
    ArrayList<AlbumState> states, otherStates;
    ProgressBar progressBar;

    private ArrayList<String> albumName, description, creator;
    private ArrayList<String> image;

    DatabaseReference allMusicDB = FirebaseDatabase.getInstance().getReference("Albums");
    FirebaseAuth musicAuth = FirebaseAuth.getInstance();
    FirebaseUser user = musicAuth.getCurrentUser();

    Button buttonCreate, buttonCloseCreate;
    FrameLayout createFrameLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlbumBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        createFrameLayout = view.findViewById(R.id.CreateAlbumFragmentContainer);
        recyclerMyView = view.findViewById(R.id.Album_music);
        recyclerAllView = view.findViewById(R.id.Album_other_music);
        buttonCloseCreate = view.findViewById(R.id.closeNewAlbumButton);
        buttonCreate = view.findViewById(R.id.newAlbumButton);
        textView11 = view.findViewById(R.id.textView11);
        progressBar = view.findViewById(R.id.progressBar5);

        albumName = new ArrayList<>();
        description = new ArrayList<>();
        creator = new ArrayList<>();
        image = new ArrayList<>();
        states = new ArrayList<>();
        otherStates = new ArrayList<>();

        allMusicDB.addValueEventListener(getAlbumData);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        recyclerAllView.setLayoutManager(manager);

        buttonCreate.setOnClickListener(v -> {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.CreateAlbumFragmentContainer, new CreateAlbumFragment());
            ft.commit();

            buttonCloseCreate.setVisibility(View.VISIBLE);
            buttonCreate.setVisibility(View.GONE);
            createFrameLayout.setVisibility(View.VISIBLE);
            recyclerAllView.setVisibility(View.GONE);
            textView11.setVisibility(View.GONE);
        });

        buttonCloseCreate.setOnClickListener(v -> {
            buttonCloseCreate.setVisibility(View.GONE);
            buttonCreate.setVisibility(View.VISIBLE);
            createFrameLayout.setVisibility(View.GONE);
            recyclerAllView.setVisibility(View.VISIBLE);
            textView11.setVisibility(View.VISIBLE);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    ValueEventListener getAlbumData = new ValueEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (states.size()>0 || albumName.size()>0 || creator.size()>0 || description.size()>0 || image.size()>0)
            {
                states.clear();
                albumName.clear();
                creator.clear();
                description.clear();
                image.clear();
                otherStates.clear();
            }
            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                Album album = dataSnapshot.getValue(Album.class);
                assert album != null;
                albumName.add(album.AlbumName);
                creator.add(album.Creator);
                description.add(album.Description);
                image.add(album.Image);
            }
            for (int i = 0; i < albumName.size(); i ++) {
                if (creator.get(i).equals(user.getDisplayName()))
                    states.add(new AlbumState(albumName.get(i), creator.get(i), description.get(i), image.get(i)
                    ));
                else otherStates.add(new AlbumState(albumName.get(i), creator.get(i), description.get(i), image.get(i)));
            }

            if (getActivity() != null) {
                adapter = new AlbumAdapter(AlbumFragment.this.getContext(), states, stateClickListener);
                otherAdapter = new AlbumAdapter(AlbumFragment.this.getContext(), otherStates, stateClickListener);
                progressBar.setVisibility(View.GONE);
                recyclerMyView.setAdapter(adapter);
                recyclerAllView.setAdapter(otherAdapter);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            progressBar.setVisibility(View.GONE);
        }
    };

    AlbumAdapter.OnStateClickListener stateClickListener = (state, position) -> {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ListenAlbumFragment fragment = new ListenAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putString("albumName", state.getAlbumName());
        bundle.putString("description", state.getDescription());
        bundle.putString("creator", state.getCreator());
        fragment.setArguments(bundle);

        ft.replace(R.id.CreateAlbumFragmentContainer,fragment);
        ft.commit();

        buttonCloseCreate.setVisibility(View.VISIBLE);
        buttonCreate.setVisibility(View.GONE);
        createFrameLayout.setVisibility(View.VISIBLE);
        recyclerAllView.setVisibility(View.GONE);
        textView11.setVisibility(View.GONE);
    };

}