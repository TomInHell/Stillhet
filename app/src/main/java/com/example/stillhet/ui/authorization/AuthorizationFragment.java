package com.example.stillhet.ui.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.stillhet.Dialog.ForgotPasswordDialog;
import com.example.stillhet.MainActivity;
import com.example.stillhet.NavigationActivity;
import com.example.stillhet.R;
import com.example.stillhet.databinding.FragmentAuthorizationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthorizationFragment extends Fragment {

    private FragmentAuthorizationBinding binding;

    Button buttonAutho;
    TextView buttonForgotThePassword;

    private final FirebaseAuth musicAuth = FirebaseAuth.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAuthorizationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        buttonAutho = root.findViewById(R.id.buttonAutho);
        buttonForgotThePassword = root.findViewById(R.id.buttonForgotThePassword);

        buttonForgotThePassword.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            EditText authoMail = root.findViewById(R.id.AuthoMail);
            String mail = authoMail.getText().toString();

            if(!TextUtils.isEmpty(mail)) {
                auth.sendPasswordResetEmail(mail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FragmentManager manager = requireActivity().getSupportFragmentManager();
                        ForgotPasswordDialog dialog = new ForgotPasswordDialog();
                        dialog.show(manager, "MyDialog");
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Проверьте правильность введенных данных", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(getActivity(), "Введите адрес электронной почты в поле 'Почта'", Toast.LENGTH_SHORT).show();
            }
        });

        buttonAutho.setOnClickListener(v -> {
            if (((MainActivity) requireActivity()).isOnline()) {
                EditText authoMail = root.findViewById(R.id.AuthoMail);
                EditText authoPass = root.findViewById(R.id.AuthoPass);

                String mail = authoMail.getText().toString().trim();
                String password = authoPass.getText().toString().trim();

                if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(password)) {
                    musicAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(requireActivity(), task -> {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = musicAuth.getCurrentUser();
                            if (user != null) {
                                requireActivity().finish();
                                Intent intent = new Intent(getActivity(), NavigationActivity.class);
                                intent.putExtra("UserName", user.getDisplayName());
                                startActivity(intent);
                            }
                        }
                        else
                            Toast.makeText(getActivity(), "Пользователь не может быть авторизован", Toast.LENGTH_SHORT).show();
                    });
                }
                else
                {
                    Toast.makeText(getActivity(), "Заполните пустые поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = musicAuth.getCurrentUser();
        if(user != null)
        {
            requireActivity().finish();
            Intent intent = new Intent(getActivity(), NavigationActivity.class);
            intent.putExtra("UserName", user.getDisplayName());
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}