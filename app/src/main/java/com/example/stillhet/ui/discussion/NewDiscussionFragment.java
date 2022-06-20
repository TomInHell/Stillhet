package com.example.stillhet.ui.discussion;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stillhet.R;
import com.example.stillhet.databinding.FragmentNewDiscussionBinding;
import com.example.stillhet.Сlasses.Discussion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewDiscussionFragment extends Fragment {

    private FragmentNewDiscussionBinding binding;
    Button buttonNewDiscussion;

    private ArrayList<String> headList;
    DatabaseReference musDB = FirebaseDatabase.getInstance().getReference("Discussion");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewDiscussionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        headList = new ArrayList<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(headList.size() > 0) {
                    headList.clear();
                }

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Discussion discussion = dataSnapshot.getValue(Discussion.class);
                    assert discussion != null;
                    headList.add(discussion.Head);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("LogFragment", "loadLog:onCancelled", error.toException());
            }
        };
        musDB.addValueEventListener(valueEventListener);

        buttonNewDiscussion = view.findViewById(R.id.buttonNewDiscussion);
        buttonNewDiscussion.setOnClickListener(v -> {
            Bundle bundle = requireActivity().getIntent().getExtras();

            Spinner ThemeDiscussion = view.findViewById(R.id.ThemeDiscussion);
            EditText HeadDiscussion = view.findViewById(R.id.HeadDiscussion);
            EditText BodyDiscussion = view.findViewById(R.id.BodyDiscussion);

            String head = HeadDiscussion.getText().toString();
            String body = BodyDiscussion.getText().toString();
            String theme = ThemeDiscussion.getSelectedItem().toString();
            String like = "";
            String creator = bundle.get("UserName").toString();

            if(!headList.contains(head)) {
                if (!TextUtils.isEmpty(head) && !TextUtils.isEmpty(body)) {
                    Discussion discussion = new Discussion(creator, head, body, theme, like);
                    musDB.child(head).setValue(discussion);

                    HeadDiscussion.getText().clear();
                    BodyDiscussion.getText().clear();

                    Toast.makeText(getActivity(), "Обсуждение создано", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Заполните пустые поля", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getActivity(), "Измените заголовок", Toast.LENGTH_SHORT).show();
            }
        });

        return  view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}