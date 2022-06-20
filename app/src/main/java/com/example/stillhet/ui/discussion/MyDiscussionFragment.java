package com.example.stillhet.ui.discussion;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stillhet.Adapter.DiscussionAdapter;
import com.example.stillhet.Adapter.SwipeToDeleteCallback;
import com.example.stillhet.R;
import com.example.stillhet.StatesForAdapter.DiscussionState;
import com.example.stillhet.databinding.FragmentMyDiscussionBinding;
import com.example.stillhet.Сlasses.Discussion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyDiscussionFragment extends Fragment {

    private FragmentMyDiscussionBinding binding;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<DiscussionState> states;
    DiscussionAdapter discussionAdapter;

    DatabaseReference musDB = FirebaseDatabase.getInstance().getReference("Discussion");

    private ArrayList<String> head, creator, body, theme, likeCount, likeString;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyDiscussionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        states = new ArrayList<>();
        recyclerView = root.findViewById(R.id.myDiscussionReView);
        progressBar = root.findViewById(R.id.progressBar);
        getData();
        enableSwipeToDeleteAndUndo();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getData()
    {
        head = new ArrayList<>();
        creator = new ArrayList<>();
        body = new ArrayList<>();
        theme = new ArrayList<>();
        likeCount = new ArrayList<>();
        likeString = new ArrayList<>();

        musDB.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (states.size()>0 || creator.size()>0 || head.size()>0 || body.size()>0 || theme.size()>0 || likeCount.size()>0 || likeString.size()>0)
                {
                    states.clear();
                    creator.clear();
                    head.clear();
                    body.clear();
                    theme.clear();
                    likeCount.clear();
                    likeString.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Discussion discussion = dataSnapshot.getValue(Discussion.class);
                    assert discussion != null;
                    head.add(discussion.Head);
                    creator.add(discussion.Creator);
                    body.add(discussion.Body);
                    theme.add(discussion.Theme);
                    likeString.add(discussion.Like);
                }

                for(int i = 0; i < likeString.size(); i++) {
                    int count = 0;
                    for (char ch : likeString.get(i).toCharArray()) {
                        if (ch == ',') {
                            count++;
                        }
                    }
                    likeCount.add(Integer.toString(count));
                }

                if(getActivity() != null) {
                    Bundle act = requireActivity().getIntent().getExtras();
                    for (int i = 0; i < head.size(); i++)
                        if (creator.get(i).equals(act.get("UserName").toString()))
                            states.add(new DiscussionState(head.get(i), theme.get(i), body.get(i), creator.get(i), likeCount.get(i)));
                }
                discussionAdapter = new DiscussionAdapter(MyDiscussionFragment.this.getContext(), states, stateClickListener);
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(discussionAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Toast.makeText(getActivity(), "Удалено", Toast.LENGTH_SHORT).show();
                int position = viewHolder.getBindingAdapterPosition();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Query query = reference.child("Discussion").orderByChild("Head").equalTo(states.get(position).getHead());
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
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    DiscussionAdapter.OnStateClickListener stateClickListener = (state, position) -> { };
}