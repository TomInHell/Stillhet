package com.example.stillhet.Dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.stillhet.R;

public class ForgotPasswordDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "~Внимание~";
        String message = "Пароль отправлен на вашу почту";

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.hint)
                .setPositiveButton("Ладушки", (dialog, id) -> dialog.cancel());
        return builder.create();
    }
}
