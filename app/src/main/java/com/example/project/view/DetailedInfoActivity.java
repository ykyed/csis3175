package com.example.project.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.project.model.Shoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class DetailedInfoActivity extends AppCompatActivity {

    private TextView txtProductName, txtProductPrice, txtStarRate;
    private ImageView imageView2;
    private Button btnCart = null;
    private GridLayout sizeButtonGrid;
    private String[] sizes = {"US 6", "US 6.5", "US 7", "US 7.5", "US 8", "US 8.5", "US 9"
            , "US 9.5", "US 10", "US 10.5", "US 11", "US 11.5", "US 12", "US 12.5", "US 13"};
    private Button selectedButton = null;

    //review part
    private LinearLayout reviewListContainer;
    private TextView reviewMessage;

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

        txtStarRate = findViewById(R.id.txtStarRate);
        txtProductName = findViewById(R.id.txtProductName);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        btnCart = findViewById(R.id.btnCart);
        sizeButtonGrid = findViewById(R.id.sizeButtonGrid);
        imageView2 = findViewById(R.id.imageView2);

        btnCart.setEnabled(false);
        createSizeButtons(sizes);

        reviewListContainer = findViewById(R.id.reviewListContainer);
        reviewMessage = findViewById(R.id.reviewMessage);

        txtStarRate.setText("" + this.getIntent().getExtras().getString("rating"));
        txtProductName.setText("" + this.getIntent().getExtras().getString("title"));
        txtProductPrice.setText("$ " + this.getIntent().getExtras().getString("price"));

        String imageUrl = this.getIntent().getExtras().getString("thumbnail");
        Glide.with(this)
                .load(imageUrl)
                .into(imageView2);
        loadShoeDataAndReviews();;
    }

    // review part
    private void loadShoeDataAndReviews() {
        String shoeDataJson = loadJSONFromAsset("shoes_data.json");
        String reviewDataJson = loadJSONFromAsset("review_data.json");

        if (shoeDataJson != null && reviewDataJson != null) {
            try {
                JSONObject shoeDataObject = new JSONObject(shoeDataJson);
                JSONArray shoesArray = shoeDataObject.getJSONArray("shoes");

                // Assuming you have a field "productCode" in your shoe data
                String productCode = this.getIntent().getExtras().getString("productCode"); // Get the productCode from the intent
                boolean hasReviews = false;

                for (int i = 0; i < shoesArray.length(); i++) {
                    JSONObject shoeObject = shoesArray.getJSONObject(i);
                    String currentProductCode = shoeObject.getString("productCode");

                    // Compare product codes
                    if (currentProductCode.equals(productCode)) {
                        hasReviews = true; // Found matching product code
                        displayReviews(reviewDataJson, productCode); // Load reviews for this product code
                        break; // Exit loop if we found the product
                    }
                }

                // Show or hide the review message based on whether there are reviews
                if (!hasReviews) {
                    reviewMessage.setVisibility(View.VISIBLE); // Show message if no matching product
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String loadJSONFromAsset(String fileName) {
        StringBuilder jsonString = new StringBuilder();
        AssetManager assetManager = getAssets(); // Get the asset manager

        try {
            InputStream inputStream = assetManager.open(fileName); // Open the file
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line); // Read each line and append it to the StringBuilder
            }
            reader.close(); // Close the reader
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace in case of an exception
        }

        return jsonString.toString(); // Return the JSON string
    }

    private void displayReviews(String reviewJson, String productCode) {
        try {
            JSONObject reviewObject = new JSONObject(reviewJson);
            JSONArray reviewsArray = reviewObject.getJSONArray("review");

            boolean foundReviews = false; // Flag to check if any reviews were found

            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject review = reviewsArray.getJSONObject(i);
                String currentProductCode = review.getString("productCode");

                // Check if the product codes match
                if (currentProductCode.equals(productCode)) {
                    foundReviews = true; // Set flag to true if we find a review
                    String title = review.getString("title");
                    String comment = review.getString("comment");
                    double rating = review.getDouble("rating");
                    addReviewToView(title, comment, rating); // Add review to view
                }
            }

            // If no reviews were found, show the message
            if (!foundReviews) {
                reviewMessage.setVisibility(View.VISIBLE);
            } else {
                reviewMessage.setVisibility(View.GONE); // Hide message if reviews were found
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addReviewToView(String title, String comment, double rating) {
        TextView reviewView = new TextView(this);
        reviewView.setText("Title: " + title + "\nRating: " + rating + "\nComment: " + comment);
        reviewView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        reviewView.setPadding(20, 10, 20, 10);

//        View horizontalLine = new View(this);
//        horizontalLine.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                2)); // Height of the line (2dp)
//        horizontalLine.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        reviewListContainer.addView(reviewView);
    }

    // button
    private void createSizeButtons(String[] sizes) {
        int totalButtons = sizes.length;
        for (int i = 0; i < totalButtons; i++) {
            Button sizeButton = createButton(sizes[i]);
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

        sizeButton.setPadding(10, 5, 10, 5);
        sizeButton.setTextSize(13);
        sizeButton.setBackgroundResource(android.R.drawable.btn_default);
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
        Button btnReview = findViewById(R.id.btnReview);

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

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedInfoActivity.this, ReviewActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black, null));

    }
}