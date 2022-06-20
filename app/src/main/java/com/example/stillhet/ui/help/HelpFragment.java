package com.example.stillhet.ui.help;

import android.content.Intent;
import android.net.Uri;
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

import com.example.stillhet.R;
import com.example.stillhet.databinding.FragmentHelpBinding;

public class HelpFragment extends Fragment {

    private FragmentHelpBinding binding;
    Button buttonHelp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHelpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        buttonHelp = root.findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(v -> {
            EditText question = root.findViewById(R.id.questionText);
            String Otv1 = question.getText().toString();
            if(!TextUtils.isEmpty(Otv1)) {
                Intent intent = new Intent(Intent.ACTION_SEND, Uri.fromParts( "mailto","stillhet2022@mail.ru", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Вопрос от пользователя");
                intent.putExtra(Intent.EXTRA_TEXT, Otv1);
                startActivity(Intent.createChooser(intent, "stillhet2022@mail.ru"));
            }
            else{
                Toast.makeText(getActivity(), "Вы не ввели ваш вопрос.", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
