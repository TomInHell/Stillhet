package com.example.stillhet.ui.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.stillhet.Dialog.RegistrationDialog;
import com.example.stillhet.MainActivity;
import com.example.stillhet.NavigationActivity;
import com.example.stillhet.R;
import com.example.stillhet.databinding.FragmentRegistrationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;

    DatabaseReference musicBD = FirebaseDatabase.getInstance().getReference("User");

    List<String> nicknames;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button buttonReg = root.findViewById(R.id.buttonReg);
        ImageView buttonRegHelp = root.findViewById(R.id.buttonRegHelp);

        nicknames = new ArrayList<>();
        ((MainActivity) requireActivity()).getDataFromDB(nicknames, musicBD);

        buttonReg.setOnClickListener(v -> {
            FirebaseAuth musicAuth = FirebaseAuth.getInstance();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child("Names");

            if (((MainActivity) requireActivity()).isOnline()) {
                EditText edName = root.findViewById(R.id.NameReg);
                EditText edMail = root.findViewById(R.id.MailReg);
                EditText edPassword = root.findViewById(R.id.PassReg);
                EditText repeatPassword = root.findViewById(R.id.PassReg2);
                Spinner mailsFor = root.findViewById(R.id.mailFor);

                String name = edName.getText().toString().trim();
                String mailWithoutDomain = edMail.getText().toString().trim();
                String domain = mailsFor.getSelectedItem().toString().trim();
                String password = edPassword.getText().toString().trim();
                String repeatPass = repeatPassword.getText().toString().trim();
                String mail = mailWithoutDomain + domain;

                if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(repeatPass)) {
                    if (!nicknames.get(0).contains(name)) {
                        if (password.length() > 5) {
                            if (!mailWithoutDomain.contains("@")) {
                                if (repeatPass.equals(password)) {

                                    musicAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = musicAuth.getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                            if (user != null) {
                                                user.updateProfile(profileUpdates);

                                                Map<String, Object> data = new HashMap<>();
                                                String newName = nicknames.get(0) + name + ",";
                                                data.put("userNames", newName);
                                                reference.updateChildren(data);

                                                requireActivity().finish();
                                                Intent intent = new Intent(getActivity(), NavigationActivity.class);
                                                intent.putExtra("UserName", name);
                                                startActivity(intent);
                                            }
                                            else
                                                Toast.makeText(getActivity(), "Что-то не так", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                            Toast.makeText(getActivity(), "Пользователь не может быть зарегистрирован, " +
                                                    "возможно введенная почта уже зарегистрирована", Toast.LENGTH_SHORT).show();
                                    });
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Неверный формат почты", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Слишком короткий пароль", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Никнейм занят", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Заполните пустые поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonRegHelp.setOnClickListener(v -> {
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            RegistrationDialog dialog = new RegistrationDialog();
            dialog.show(manager, "MyDialog");
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}