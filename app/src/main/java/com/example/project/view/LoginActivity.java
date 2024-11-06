package com.example.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;
import com.example.project.model.DBHelper;

public class LoginActivity extends ToolbarLogoBaseActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FrameLayout contentFrame = findViewById(R.id.contentFrame);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_login, contentFrame, false);
        contentFrame.addView(contentView);


        // Initialize DBHelper for accessing user data
        dbHelper = DBHelper.getInstance(this);

        // Initialize UI components
        usernameField = findViewById(R.id.email_input);
        passwordField = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.sign_in_button);

        // Set login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
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
        boolean isAuthenticated = dbHelper.checkUserCredentials(username, password);
        if (isAuthenticated) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            // Proceed to the next activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class); // MainActivity is your next screen
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
