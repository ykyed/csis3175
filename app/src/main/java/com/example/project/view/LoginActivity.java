package com.example.project.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.model.UserInfoDAO;

import java.util.Map;

public class LoginActivity extends ToolbarLogoBaseActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrame = findViewById(R.id.contentFrame);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_login, contentFrame, false);
        contentFrame.addView(contentView);

        // Initialize UI components
        usernameField = findViewById(R.id.email_input);
        passwordField = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.sign_in_button);
        signUp = findViewById(R.id.signUp);

        // Set login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showSignupDialog();
                SignupFragment signupFragment = new SignupFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, signupFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    private void attemptLogin() {
        // Get input from fields
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Check if inputs are empty
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

        // Authenticate user (you need a method in DBHelper for this)
        //boolean isAuthenticated = dbHelper.checkUserCredentials(username, password);
        UserInfoDAO userInfoDAO = new UserInfoDAO(this);
        Map<String, String> userInfo = userInfoDAO.signIn(username, password);

        //if (isAuthenticated) {
        if (userInfo != null) {
            Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show();

            // add to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.user_info_shared_preference), MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString(getResources().getString(R.string.key_first_name), userInfo.get(getResources().getString(R.string.key_first_name)));
            myEdit.putString(getResources().getString(R.string.key_email), userInfo.get(getResources().getString(R.string.key_email)));
            myEdit.apply();

            // Proceed to the next activity
            //Intent intent = new Intent(LoginActivity.this, MainActivity.class); // MainActivity is your next screen
            //startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
