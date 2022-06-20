package com.example.stillhet.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.stillhet.MainActivity;
import com.example.stillhet.Сlasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String title = "~ВНИМАНИЕ~";
        String message = "Вы уверены, что хотите удалить ваш аккаунт?";
        String buttonYes = "Да";
        String buttonNo = "Нет";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonYes, (dialog, which) -> {
            Bundle argument = requireActivity().getIntent().getExtras();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("Discussion").orderByChild("Creator").equalTo(argument.get("UserName").toString());
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

            Query query1 = reference.child("Albums").orderByChild("Creator").equalTo(argument.get("UserName").toString());
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
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

            Query query2 = reference.child(argument.get("UserName").toString());
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
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

            List<String> name = new ArrayList<>();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren())
                    {
                        User user = ds.getValue(User.class);
                        assert user != null;
                        name.add(user.userNames);
                    }
                    DatabaseReference referenceUserName = FirebaseDatabase.getInstance().getReference("User").child("Names");
                    Map<String, Object> data = new HashMap<>();
                    String oldLine = name.get(0);
                    String delete = argument.get("UserName").toString() + ",";
                    data.put("userNames", oldLine.replace(delete, ""));
                    referenceUserName.updateChildren(data);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null)
                user.delete();
            FirebaseAuth musicAuth = FirebaseAuth.getInstance();
            musicAuth.signOut();
            requireActivity().finish();
            startActivity(new Intent(getActivity(), MainActivity.class));
        });
        builder.setNegativeButton(buttonNo, (dialog, which) -> dialog.cancel());
        builder.setCancelable(true);

        return  builder.create();
    }
}
