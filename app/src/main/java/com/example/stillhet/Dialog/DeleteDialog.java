package com.example.stillhet.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.stillhet.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
