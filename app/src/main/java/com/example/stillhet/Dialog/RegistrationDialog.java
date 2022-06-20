package com.example.stillhet.Dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.stillhet.R;

public class RegistrationDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String title = "~ПОДСКАЗКА~";
        String message = "1. Ваш никнейм должен быть уникален. Вы не сможете изменить его в будущем. " + System.lineSeparator() +
                "2. Ваш адрес электронной почты должен быть действителен(в случае, " +
                "если вы забудете пароль, ссылка для восстановления будет направлена на вашу почту)." +
                " Вы не сможете изменить адрес посде регистрации. " + System.lineSeparator() +
                "3. Ваш пароль должен содержать более 5 символов. Вы сможете изменить пароль в любой момент." + System.lineSeparator() +
                "4. Если у вас возникли проблемы, напишите письмо на нашу почту: stillhet2022@mail.ru";

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.hint)
                .setPositiveButton("Ладушки", (dialog, id) -> dialog.cancel());
        return builder.create();
    }
}
