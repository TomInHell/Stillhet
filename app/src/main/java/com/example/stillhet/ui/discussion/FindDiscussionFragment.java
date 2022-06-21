package com.example.stillhet.ui.discussion;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stillhet.Adapter.DiscussionAdapter;
import com.example.stillhet.R;
import com.example.stillhet.StatesForAdapter.DiscussionState;
import com.example.stillhet.databinding.FindDiscussionFragmentBinding;
import com.example.stillhet.Сlasses.Discussion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindDiscussionFragment extends Fragment {

    FindDiscussionFragmentBinding binding;

    RecyclerView recyclerView;
    EditText findLine;
    ProgressBar progressBar;
    ArrayList<DiscussionState> states;
    DiscussionAdapter discussionAdapter;

    String Key = "Discussion";
    DatabaseReference musDB = FirebaseDatabase.getInstance().getReference(Key);

    private ArrayList<String> head, creator, body, theme, likeString, likeCount;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FindDiscussionFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = view.findViewById(R.id.Find_recycler);
        progressBar = view.findViewById(R.id.progressBar2);

        getDiscussionData();

        findLine = view.findViewById(R.id.findLine);
        findLine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(findLine.getText().toString().equals(""))
                    getDiscussionData();
                else
                    getDiscussionDataOnTextChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getDiscussionData() {
        Runnable runnable = () -> {
            head = new ArrayList<>();
            creator = new ArrayList<>();
            body = new ArrayList<>();
            theme = new ArrayList<>();
            likeString = new ArrayList<>();
            likeCount = new ArrayList<>();
            states = new ArrayList<>();

            ValueEventListener valueEventListener = new ValueEventListener() {
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

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
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

                    for (int i = 0; i < head.size(); i ++)
                        states.add(new DiscussionState(head.get(i), theme.get(i), body.get(i), creator.get(i), likeCount.get(i)));

                    if(getActivity() != null) {
                        discussionAdapter = new DiscussionAdapter(FindDiscussionFragment.this.getContext(), states, stateClickListener);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setAdapter(discussionAdapter);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                }
            };
            musDB.addValueEventListener(valueEventListener);
        };
       Thread thread = new Thread(runnable);
       thread.start();
    }

    private void getDiscussionDataOnTextChanged() {
        Runnable runnable = () -> {
            head = new ArrayList<>();
            creator = new ArrayList<>();
            body = new ArrayList<>();
            theme = new ArrayList<>();
            likeString = new ArrayList<>();
            likeCount = new ArrayList<>();
            states = new ArrayList<>();

            ValueEventListener valueEventListener = new ValueEventListener() {
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

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
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

                    for (int i = 0; i < head.size(); i ++)
                        if(head.get(i).contains(findLine.getText()) || creator.get(i).contains(findLine.getText()) || theme.get(i).contains(findLine.getText()) ||
                                head.get(i).toLowerCase().contains(findLine.getText()) || creator.get(i).toLowerCase().contains(findLine.getText()) || theme.get(i).toLowerCase().contains(findLine.getText()) ||
                                head.get(i).toUpperCase().contains(findLine.getText()) || creator.get(i).toUpperCase().contains(findLine.getText()) || theme.get(i).toUpperCase().contains(findLine.getText()))
                            states.add(new DiscussionState(head.get(i), theme.get(i), body.get(i), creator.get(i), likeCount.get(i)));
                    if(getActivity() != null) {
                        discussionAdapter = new DiscussionAdapter(FindDiscussionFragment.this.getContext(), states, stateClickListener);
                        recyclerView.setAdapter(discussionAdapter);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("LogFragment", "loadLog:onCancelled", error.toException());
                }
            };
            musDB.addValueEventListener(valueEventListener);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    DiscussionAdapter.OnStateClickListener stateClickListener = new DiscussionAdapter.OnStateClickListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onStateClick(DiscussionState state, int position) {

            Bundle bundle = requireActivity().getIntent().getExtras();
            String userName = bundle.get("UserName").toString();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Discussion").child(head.get(position));

            Map<String, Object> data = new HashMap<>();

            if(creator.get(position).equals(userName)) {
                Toast.makeText(getActivity(), "Вы не можете оценить свою запись", Toast.LENGTH_SHORT).show();
            }
            else if(likeString.get(position).contains(userName)) {
                String oldLine = likeString.get(position);
                String delete = userName + ",";
                data.put("Like", oldLine.replace(delete, ""));

                reference.updateChildren(data);
            }
            else if(!likeString.get(position).contains(userName)) {
                String newLike = likeString.get(position) + userName + ",";
                data.put("Like", newLike);

                reference.updateChildren(data);
            }
        }
    };
}
