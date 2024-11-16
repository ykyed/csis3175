package com.example.project.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.project.R;
import com.example.project.model.UserInfoDAO;

import java.util.Map;

public class LoginActivity extends ToolbarLogoBaseActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private UserInfoDAO userInfoDAO;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrame = findViewById(R.id.contentFrame);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_login, contentFrame, false);
        contentFrame.addView(contentView);

        userInfoDAO = new UserInfoDAO(this);

        usernameField = findViewById(R.id.email_input);
        passwordField = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.sign_in_button);
        signUp = findViewById(R.id.signUp);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupFragment signupFragment = new SignupFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, signupFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void openForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        final EditText input = new EditText(this);
        input.setHint("Enter your email");
        builder.setView(input);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString().trim();
                if (!email.isEmpty()) {
                    handlePasswordReset(email);
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void handlePasswordReset(String email) {
        if (userInfoDAO.doesUserExist(email)) {
            // Provide further instructions or send a password reset email
            Toast.makeText(this, "Password reset instructions sent to " + email, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No account found with that email", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void attemptLogin() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (username.isEmpty()) {
            usernameField.setError("Username is required");
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordField.setError("Password is required");
            passwordField.requestFocus();
            return;
        }

        Map<String, String> userInfo = userInfoDAO.signIn(username, password);

        if (userInfo != null) {
            String welcomeMessage = "Welcome, " + userInfo.get(getString(R.string.key_first_name));
            Toast.makeText(this, welcomeMessage, Toast.LENGTH_SHORT).show();

            // add to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.user_info_shared_preference), MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString(getResources().getString(R.string.key_first_name), userInfo.get(getResources().getString(R.string.key_first_name)));
            myEdit.putString(getResources().getString(R.string.key_email), userInfo.get(getResources().getString(R.string.key_email)));
            myEdit.apply();


            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}