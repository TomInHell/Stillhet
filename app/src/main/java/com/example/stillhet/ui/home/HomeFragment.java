package com.example.stillhet.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.stillhet.Dialog.ChangeMailDialog;
import com.example.stillhet.Dialog.ChangePassDialog;
import com.example.stillhet.MainActivity;
import com.example.stillhet.R;
import com.example.stillhet.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Button buttonExit, buttonChangePass, buttonChangeMail;

    private final FirebaseAuth musicAuth = FirebaseAuth.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = requireActivity().getIntent().getExtras();
        FirebaseUser user = musicAuth.getCurrentUser();

        EditText mail = binding.AccountMail;
        EditText name = binding.AccountName;
        if (user != null) {
            mail.setText(user.getEmail());
            name.setText(bundle.get("UserName").toString());
        }

        buttonExit = root.findViewById(R.id.buttonExit);
        buttonChangePass = root.findViewById(R.id.buttonChangePass);
        buttonChangeMail = root.findViewById(R.id.buttonChangeMail);

        buttonExit.setOnClickListener(v -> {
            requireActivity().finish();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            musicAuth.signOut();
        });

        buttonChangeMail.setOnClickListener(v -> {
            EditText accMail = root.findViewById(R.id.AccountMail);
            String mail1 = accMail.getText().toString();

            if(!TextUtils.isEmpty(mail1)) {
                if(mail1.contains("@mail.ru") || mail1.contains("@gmail.com") || mail1.contains("@list.ru") || mail1.contains("@yandex.ru")) {
                        FragmentManager manager = requireActivity().getSupportFragmentManager();
                        ChangeMailDialog dialog = new ChangeMailDialog();
                        dialog.show(manager, "MyDialog");
                }
                else Toast.makeText(getActivity(), "Формат почты не совпадает", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getActivity(), "Вы не ввели почту", Toast.LENGTH_SHORT).show();
        });

        buttonChangePass.setOnClickListener(v -> {
            EditText accPass = root.findViewById(R.id.AccountPassword);
            String password = accPass.getText().toString();

            if(!TextUtils.isEmpty(password)) {
                if(password.length() > 5) {
                    FragmentManager manager = requireActivity().getSupportFragmentManager();
                    ChangePassDialog dialog = new ChangePassDialog();
                    dialog.show(manager, "MyDialog");
                }
                else Toast.makeText(getActivity(), "Слишком короткий пароль", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getActivity(), "Вы не ввели пароль", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
}