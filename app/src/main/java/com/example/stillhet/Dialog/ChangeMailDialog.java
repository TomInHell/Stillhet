package com.example.stillhet.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.stillhet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeMailDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String title = "~ВНИМАНИЕ~";
        String message = "Вы уверены, что хотите изменить адрес электронной почты?";
        String buttonYes = "Да";
        String buttonNo = "Нет";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonYes, (dialog, which) -> {

            EditText mail = requireActivity().findViewById(R.id.AccountMail);

            dialog.cancel();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null)
                user.updateEmail(mail.getText().toString().trim()).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        dialog.cancel();
                });
        });
        builder.setNegativeButton(buttonNo, (dialog, which) -> dialog.cancel());
        builder.setCancelable(true);

        return  builder.create();
    }
}
