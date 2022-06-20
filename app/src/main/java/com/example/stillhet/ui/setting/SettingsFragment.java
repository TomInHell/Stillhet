package com.example.stillhet.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.stillhet.Dialog.DeleteDialog;
import com.example.stillhet.R;
import com.example.stillhet.databinding.FragmentSettingBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingBinding binding;
    Button buttonDelete;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        buttonDelete = root.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(v -> {
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            DeleteDialog dialog = new DeleteDialog();
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