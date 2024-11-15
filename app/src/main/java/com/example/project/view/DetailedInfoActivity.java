package com.example.project.view;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.project.model.CartInfo;
import com.example.project.model.CartInfoDAO;
import com.example.project.model.ReviewInfo;
import com.example.project.model.ReviewInfoDAO;
import com.example.project.model.Shoe;
import com.example.project.model.ShoeDAO;
import com.example.project.model.SizeInfo;
import com.example.project.model.SizeInfoDAO;

import java.util.ArrayList;


public class DetailedInfoActivity extends ToolbarBaseActivity {

    private TextView txtProductName, txtProductPrice, txtStarRate;
    private RatingBar ratingBar;
    private ImageView imageView2;
    private Button btnCart = null;
    private GridLayout sizeButtonGrid;
    private Button selectedButton = null;

    //review part
    private LinearLayout reviewListContainer;
    private TextView reviewMessage;
    private Button btnReview;

    private CartInfoDAO cartInfoDAO;

    private ShoeDAO shoeDAO;
    private ReviewInfoDAO reviewInfoDAO;
    private String productCode;
    private Shoe shoeInfo;

    private SizeInfoDAO sizeInfoDAO;
    private ArrayList<SizeInfo> sizeInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrame = findViewById(R.id.contentFrame);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_detailed_info, contentFrame, false);
        contentFrame.addView(contentView);

        cartInfoDAO = new CartInfoDAO(this);

        shoeDAO = new ShoeDAO(this);
        reviewInfoDAO = new ReviewInfoDAO(this);

        productCode = getIntent().getStringExtra(getString(R.string.key_product_code));
        shoeInfo = shoeDAO.getShoe(productCode);

        sizeInfoDAO = new SizeInfoDAO(this);

        sizeInfos = (ArrayList<SizeInfo>)sizeInfoDAO.getSizesByProductCode(productCode);

        initLayout();
    }

    private void initLayout() {
        txtStarRate = findViewById(R.id.txtStarRate);
        ratingBar = findViewById(R.id.ratingBar);
        txtProductName = findViewById(R.id.txtProductName);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        imageView2 = findViewById(R.id.imageView2);

        btnCart = findViewById(R.id.btnCart);
        btnCart.setEnabled(false);

        SharedPreferences sh = getSharedPreferences(getResources().getString(R.string.user_info_shared_preference), MODE_PRIVATE);
        String userEmail = sh.getString(getResources().getString(R.string.key_email), "");

        String userName = sh.getString(getResources().getString(R.string.key_first_name), "");

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userEmail.isEmpty()) {
                    CartInfo cartInfo = new CartInfo();
                    cartInfo.setProductCode(shoeInfo.getProductCode());
                    cartInfo.setEmail(userEmail);
                    cartInfo.setSize(Double.parseDouble(selectedButton.getText().toString().replace("US ", "")));
                    cartInfo.setQuantity(1);

                    cartInfoDAO.addItem(cartInfo);

                    updateToolbar(userName, userEmail);

                } else {
                    Intent intent = new Intent(DetailedInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        sizeButtonGrid = findViewById(R.id.sizeButtonGrid);
        createSizeButtons();

        reviewListContainer = findViewById(R.id.reviewListContainer);
        reviewMessage = findViewById(R.id.reviewMessage);

        txtStarRate.setText(String.valueOf(shoeInfo.getRating()));
        txtProductName.setText(shoeInfo.getTitle());
        txtProductPrice.setText("$ " + shoeInfo.getPrice());

        String imageUrl = shoeInfo.getThumbnail();
        Glide.with(this)
                .load(imageUrl)
                .into(imageView2);

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
        reviewListContainer.removeAllViews();
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

        TextView reviewView = new TextView(this);
        reviewView.setText("Title: " + title + "\nRating: " + rating + "\nComment: " + comment);
        reviewView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        reviewView.setPadding(20, 10, 20, 10);

        reviewListContainer.addView(reviewView);
    }

    // button
    private void createSizeButtons() {
        int totalButtons = sizeInfos.size();
        for (int i = 0; i < totalButtons; i++) {
            Button sizeButton = createButton( "US " + sizeInfos.get(i).getSize(), sizeInfos.get(i).getQuantity());
            sizeButtonGrid.addView(sizeButton);
        }
    }

    private Button createButton(String size, int quantity) {

        Button sizeButton = new Button(this);
        sizeButton.setText(size);
        if(quantity == 0) {
            sizeButton.setEnabled(false);
        }

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 180;
        params.height = 100;
        sizeButton.setLayoutParams(params);

        sizeButton.setPadding(10, 5, 10, 5);
        sizeButton.setTextSize(13);
        sizeButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.size_btn_background));

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
            selectedButton.setTextColor(ContextCompat.getColorStateList(this, R.color.black));
            selectedButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.size_btn_background));
        }
        clickedButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        clickedButton.setTextColor(ContextCompat.getColorStateList(this, R.color.white));
        selectedButton = clickedButton;

        Button addToCartButton = findViewById(R.id.btnCart);
        addToCartButton.setEnabled(true);
        addToCartButton.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}