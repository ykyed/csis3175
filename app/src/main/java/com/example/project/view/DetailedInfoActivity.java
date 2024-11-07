package com.example.project.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.model.ReviewInfo;
import com.example.project.model.ReviewInfoDAO;
import com.example.project.model.Shoe;
import com.example.project.model.ShoeDAO;

import java.util.ArrayList;


public class DetailedInfoActivity extends ToolbarBaseActivity {

    private TextView txtProductName, txtProductPrice, txtStarRate;
    private RatingBar ratingBar;
    private ImageView imageView2;
    private Button btnCart = null;
    private GridLayout sizeButtonGrid;
    private String[] sizes = {"US 6", "US 6.5", "US 7", "US 7.5", "US 8", "US 8.5", "US 9"
            , "US 9.5", "US 10", "US 10.5", "US 11", "US 11.5", "US 12", "US 12.5", "US 13"};
    private Button selectedButton = null;

    //review part
    private LinearLayout reviewListContainer;
    private TextView reviewMessage;
    private Button btnReview;

    private ShoeDAO shoeDAO;
    private ReviewInfoDAO reviewInfoDAO;
    private String productCode;
    private Shoe shoeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrame = findViewById(R.id.contentFrame);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_detailed_info, contentFrame, false);
        contentFrame.addView(contentView);

        shoeDAO = new ShoeDAO(this);
        reviewInfoDAO = new ReviewInfoDAO(this);

        productCode = getIntent().getStringExtra(getString(R.string.key_product_code));
        shoeInfo = shoeDAO.getShoe(productCode);

        initLayout();
    }

    private void initLayout() {
        txtStarRate = findViewById(R.id.txtStarRate);
        ratingBar = findViewById(R.id.ratingBar);
        txtProductName = findViewById(R.id.txtProductName);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        btnCart = findViewById(R.id.btnCart);
        sizeButtonGrid = findViewById(R.id.sizeButtonGrid);
        imageView2 = findViewById(R.id.imageView2);

        btnCart.setEnabled(false);
        createSizeButtons(sizes);

        reviewListContainer = findViewById(R.id.reviewListContainer);
        reviewMessage = findViewById(R.id.reviewMessage);

        txtStarRate.setText(String.valueOf(shoeInfo.getRating()));
        txtProductName.setText(shoeInfo.getTitle());
        txtProductPrice.setText("$ " + shoeInfo.getPrice());

        String imageUrl = shoeInfo.getThumbnail();
        Glide.with(this)
                .load(imageUrl)
                .into(imageView2);
        //loadShoeDataAndReviews();;

        btnReview = findViewById(R.id.btnReview);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedInfoActivity.this, ReviewActivity.class);
                intent.putExtra("product_code", shoeInfo.getProductCode());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ratingBar.setRating((float) shoeInfo.getRating());

        displayReviews();
    }

    private void displayReviews() {

        if (shoeInfo.getReviewCount() > 0) {
            // show review
            reviewMessage.setVisibility(View.GONE);
            ArrayList<ReviewInfo> reviews = reviewInfoDAO.getReviewsByPrductCode(productCode);
            if (reviews != null) {
                for(int i = 0; i < reviews.size(); i++) {
                    addReviewToView(reviews.get(i).getTitle(), reviews.get(i).getComment(), reviews.get(i).getRating()) ;
                }
            }
        }
        else {
            reviewMessage.setVisibility(View.VISIBLE);
        }
    }

    private void addReviewToView(String title, String comment, double rating) {
        reviewListContainer.removeAllViews();

        TextView reviewView = new TextView(this);
        reviewView.setText("Title: " + title + "\nRating: " + rating + "\nComment: " + comment);
        reviewView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        reviewView.setPadding(20, 10, 20, 10);

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
}