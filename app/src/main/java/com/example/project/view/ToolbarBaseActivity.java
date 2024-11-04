package com.example.project.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;
import com.example.project.model.CartInfo;
import com.example.project.model.CartInfoDAO;
import com.example.project.model.UserInfoDAO;

import java.util.ArrayList;
import java.util.Map;

public class ToolbarBaseActivity extends AppCompatActivity {

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

        // this is the temp code for sing in
        UserInfoDAO userInfoDAO = new UserInfoDAO(this);
        Map<String, String> userInfo = userInfoDAO.signIn("wooastudio1012@gmail.com", "Qwer1234");

        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.user_info_shared_preference), MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(getResources().getString(R.string.key_first_name), userInfo.get(getResources().getString(R.string.key_first_name)));
        myEdit.putString(getResources().getString(R.string.key_email), userInfo.get(getResources().getString(R.string.key_email)));
        myEdit.apply();

        initActionbarLayout();
    }

    private void initActionbarLayout() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton imgLogin = findViewById(R.id.imgLogin);
        ImageButton imgCart = findViewById(R.id.imgCart);
        TextView txtName = findViewById(R.id.txtName);
        TextView txtCartBadge = findViewById(R.id.txtCartBadge);

        SharedPreferences sh = getSharedPreferences(getResources().getString(R.string.user_info_shared_preference), MODE_PRIVATE);
        String userName = sh.getString(getResources().getString(R.string.key_first_name), "");
        String userEmail = sh.getString(getResources().getString(R.string.key_email), "");

        if (!userName.isEmpty()) {
            if (userName.length() > 3) {
                userName = userName.substring(0, 2);
            }
            String text = "Hi, " + userName;
            txtName.setText(text);
            txtName.setVisibility(View.VISIBLE);
        }
        else {
            txtName.setVisibility(View.GONE);
        }

        CartInfoDAO cartInfoDAO = new CartInfoDAO(this);
        ArrayList<CartInfo> cartInfos = cartInfoDAO.getCartItemsByUser(userEmail);
        if (!cartInfos.isEmpty()) {
            txtCartBadge.setText(String.valueOf(cartInfos.size()));
            txtCartBadge.setVisibility(View.VISIBLE);
        }
        else {
            txtCartBadge.setVisibility(View.GONE);
        }

        imgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToolbarBaseActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToolbarBaseActivity.this, CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black, null));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
    }
}