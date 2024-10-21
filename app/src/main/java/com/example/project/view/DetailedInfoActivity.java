package com.example.project.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.project.R;

public class DetailedInfoActivity extends AppCompatActivity {

    private TextView txtProductName;
    private ImageView imageView2;
    private Button btnCart = null;
    private GridLayout sizeButtonGrid;
    private String[] sizes = {"US 6", "US 6.5", "US 7", "US 7.5", "US 8", "US 8.5", "US 9"
            , "US 9.5", "US 10", "US 10.5", "US 11", "US 11.5", "US 12", "US 12.5", "US 13"};
    private Button selectedButton = null;
    private Color sizeBtnBgColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initActionbarLayout();

        txtProductName = findViewById(R.id.txtProductName);
        btnCart = findViewById(R.id.btnCart);
        sizeButtonGrid = findViewById(R.id.sizeButtonGrid);
        imageView2 = findViewById(R.id.imageView2);

        btnCart.setEnabled(false);
        createSizeButtons(sizes);
        txtProductName.setText("" + this.getIntent().getExtras().getString("productCode"));
        //System.out.println(this.getIntent().getExtras().getBundle("productCode"));
        //intent.putExtra("productCode", selectedItem.getProductCode());

        String imageUrl = this.getIntent().getExtras().getString("thumbnail");
        Glide.with(this)
                .load(imageUrl)
                .into(imageView2);
    }

    private void createSizeButtons(String[] sizes) {
        int totalButtons = sizes.length;
        for (int i = 0; i < totalButtons; i++) {
            Button sizeButton = createButton(sizes[i]);
            //sizeBtnBgColor = sizeButton.getBackgroundTintList();
            sizeButtonGrid.addView(sizeButton);
        }
    }

    private ColorStateList orgSizeBtnColorStateList;

    private Button createButton(String size) {
        Button sizeButton = new Button(this);
        sizeButton.setText(size);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 180;
        params.height = 100;
        sizeButton.setLayoutParams(params);

//        sizeButton.setPadding(3, 5, 3, 5);
        sizeButton.setPadding(10, 5, 10, 5);
        sizeButton.setTextSize(13);
        sizeButton.setBackgroundResource(android.R.drawable.btn_default);
        //sizeButton.setBackgroundColor(Color.WHITE);
        //sizeButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
        orgSizeBtnColorStateList = sizeButton.getBackgroundTintList();

        sizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSizeButtonClicked(sizeButton);
            }
        });

        return sizeButton;
    }

    private void onSizeButtonClicked(Button clickedButton) {
        if (selectedButton != null) {
            selectedButton.setEnabled(true);
            //selectedButton.setBackgroundResource(android.R.drawable.btn_default);
            //selectedButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
            selectedButton.setBackgroundTintList(orgSizeBtnColorStateList);
        }
        //clickedButton.setEnabled(false);
        clickedButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
        selectedButton = clickedButton;

        Button addToCartButton = findViewById(R.id.btnCart);
        addToCartButton.setEnabled(true);
        addToCartButton.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
    }

    private void updateSelectedButton(Button clickedButton) {
        if (selectedButton != null) {
            selectedButton.setBackgroundColor(Color.TRANSPARENT);  // Reset color
        }

        clickedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        selectedButton = clickedButton;
    }

    private void enableAddToCartButton(boolean isEnabled) {
        btnCart.setEnabled(isEnabled);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void initActionbarLayout() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton imgLogin = findViewById(R.id.imgLogin);
        ImageButton imgCart = findViewById(R.id.imgCart);

        imgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedInfoActivity.this, CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black, null));


    }
}