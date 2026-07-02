package com.example.aplicacion0102;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegisterUserFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_user, container, false);

        EditText editName = view.findViewById(R.id.editUserName);
        EditText editEmail = view.findViewById(R.id.editUserEmail);
        Button btnSave = view.findViewById(R.id.btnSaveUser);

        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String email = editEmail.getText().toString();

            if (!name.isEmpty() && !email.isEmpty()) {
                User user = new User(name, email);
                
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    AppDatabase.getDatabase(getContext()).userDao().insert(user);
                    
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), R.string.user_registered, Toast.LENGTH_SHORT).show();
                            editName.setText("");
                            editEmail.setText("");
                        });
                    }
                });
            }
        });

        return view;
    }
}