package com.example.project.view;

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

import com.example.project.R;
import com.example.project.model.UserInfo;
import com.example.project.model.UserInfoDAO;

public class SignupFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        EditText txtFName = view.findViewById(R.id.txtFName);
        EditText txtLName = view.findViewById(R.id.txtLName);
        EditText txtEmail = view.findViewById(R.id.txtEmail);
        EditText txtPassword = view.findViewById(R.id.txtPassword);
        Button btnApply = view.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(v -> {
            String fName = txtFName.getText().toString();
            String lName = txtLName.getText().toString();
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();

            if (fName.isEmpty()) {
                txtFName.setError("First name is required");
                txtFName.requestFocus();
                return;
            }
            else if (lName.isEmpty()) {
                txtLName.setError("Last name is required");
                txtLName.requestFocus();
                return;
            }
            else if (email.isEmpty()) {
                txtEmail.setError("Email address is required");
                txtEmail.requestFocus();
                return;
            }
            else if (password.isEmpty()) {
                txtPassword.setError("Password is required");
                txtPassword.requestFocus();
                return;
            }

            UserInfoDAO userInfoDAO = new UserInfoDAO(getActivity());
            userInfoDAO.addUser(new UserInfo(email, password, fName, lName));
            getParentFragmentManager().popBackStack();

            Toast.makeText(getActivity(), "Create Account successful", Toast.LENGTH_SHORT).show();
        });
        return view;
    }
}
