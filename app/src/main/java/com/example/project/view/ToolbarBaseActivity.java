package com.example.project.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;
import com.example.project.model.CartInfo;
import com.example.project.model.CartInfoDAO;

import java.util.ArrayList;

public class ToolbarBaseActivity extends AppCompatActivity {

    private CartInfoDAO cartInfoDAO;
    private TextView txtName, txtCartBadge;
    private ImageButton imgBack;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_toolbar_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cartInfoDAO = new CartInfoDAO(this);
        initActionbarLayout();
    }

    private void initActionbarLayout() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton imgLogin = findViewById(R.id.imgLogin);
        ImageButton imgCart = findViewById(R.id.imgCart);
        imgBack = findViewById(R.id.imgBack);
        txtName = findViewById(R.id.txtName);
        txtCartBadge = findViewById(R.id.txtCartBadge);

        imgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    showDialog();
                }
                else {
                    Intent intent = new Intent(ToolbarBaseActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    Intent intent = new Intent(ToolbarBaseActivity.this, CartActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                else {
                    Intent intent = new Intent(ToolbarBaseActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getSupportFragmentManager().getFragments().isEmpty()) {
                    finish();
                }
                else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black, null));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sh = getSharedPreferences(getResources().getString(R.string.user_info_shared_preference), MODE_PRIVATE);
        String userName = sh.getString(getResources().getString(R.string.key_first_name), "");
        String userEmail = sh.getString(getResources().getString(R.string.key_email), "");

        updateToolbar(userName, userEmail);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setTitle("Sign out");
        builder.setMessage("Do you want to sign out ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.user_info_shared_preference), MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString(getResources().getString(R.string.key_first_name), "");
            myEdit.putString(getResources().getString(R.string.key_email), "");
            myEdit.apply();

            updateToolbar("", "");

            Toast.makeText(this, "Sign out successful.", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    protected void updateToolbar(String userName, String userEmail) {

        if (!userName.isEmpty()) {
            if (userName.length() > 3) {
                userName = userName.substring(0, 3);
            }
            String text = "Hi, " + userName;
            txtName.setText(text);
            txtName.setVisibility(View.VISIBLE);
            isLogin = true;
        }
        else {
            txtName.setVisibility(View.GONE);
            isLogin = false;
        }

        if(!userEmail.isEmpty()) {
            ArrayList<CartInfo> cartInfos = cartInfoDAO.getCartItemsByUser(userEmail);
            if (!cartInfos.isEmpty()) {
                txtCartBadge.setText(String.valueOf(cartInfos.size()));
                txtCartBadge.setVisibility(View.VISIBLE);
            }
            else {
                txtCartBadge.setVisibility(View.GONE);
            }
        }
        else {
            txtCartBadge.setVisibility(View.GONE);
        }
    }

    protected void setBackButtonVisibility(int visibility) {
        imgBack.setVisibility(visibility);
    }
}