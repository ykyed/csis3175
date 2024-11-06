package com.example.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.R;
import com.example.project.model.DBHelper;
import com.example.project.model.ReviewInfo;
import com.example.project.model.ReviewInfoDAO;
import com.example.project.model.Shoe;
import com.example.project.model.ShoeDAO;

public class ReviewActivity extends ToolbarLogoBaseActivity {

    //private ImageView productImage;
    private TextView productName;
    private RatingBar ratingBar;
    private EditText reviewHeadline;
    private EditText reviewComment;
    private Button submitReviewButton;
    //private DBHelper dbHelper;
    private String productCode;

    private ShoeDAO shoeDAO;
    private ReviewInfoDAO reviewInfoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrame = findViewById(R.id.contentFrame);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_review, contentFrame, false);
        contentFrame.addView(contentView);

        // DBHelper
        //dbHelper = DBHelper.getInstance(this);
        reviewInfoDAO = new ReviewInfoDAO(this);
        shoeDAO = new ShoeDAO(this);

        productCode = getIntent().getStringExtra("product_code");
        Shoe shoe = shoeDAO.getShoe(productCode);


        productName = findViewById(R.id.product_name);
        ratingBar = findViewById(R.id.rating_bar);
        reviewHeadline = findViewById(R.id.review_headline);
        reviewComment = findViewById(R.id.review_comment);
        submitReviewButton = findViewById(R.id.submit_review);

        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });


        productName.setText(shoe.getTitle());

    }

    private void submitReview() {
        float rating = ratingBar.getRating();
        String headline = reviewHeadline.getText().toString().trim();
        String comment = reviewComment.getText().toString().trim();

        // Validate inputs
        if (rating == 0) {
            Toast.makeText(this, "Please provide a rating.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (headline.isEmpty()) {
            Toast.makeText(this, "Please provide a review headline.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (comment.isEmpty()) {
            Toast.makeText(this, "Please provide a comment.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert review into database
        //dbHelper.addReview(shoeId, rating, headline, comment);
        reviewInfoDAO.addReview(new ReviewInfo());


        // Show a confirmation message
        Toast.makeText(this, "Review submitted successfully!", Toast.LENGTH_SHORT).show();

        // Clear input fields
        ratingBar.setRating(0);
        reviewHeadline.setText("");
        reviewComment.setText("");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
