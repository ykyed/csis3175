package com.example.project.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.model.ReviewInfo;
import com.example.project.model.ReviewInfoDAO;
import com.example.project.model.Shoe;
import com.example.project.model.ShoeDAO;

public class ReviewActivity extends ToolbarLogoBaseActivity {

    private TextView productName;
    private RatingBar ratingBar;
    private EditText reviewHeadline, reviewComment;
    private Button submitReviewButton;
    private ImageView imgThumb;
    private String productCode;

    private ShoeDAO shoeDAO;
    private ReviewInfoDAO reviewInfoDAO;

    private Shoe shoeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrame = findViewById(R.id.contentFrame);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_review, contentFrame, false);
        contentFrame.addView(contentView);

        reviewInfoDAO = new ReviewInfoDAO(this);
        shoeDAO = new ShoeDAO(this);

        productCode = getIntent().getStringExtra("product_code");
        shoeInfo = shoeDAO.getShoe(productCode);

        productName = findViewById(R.id.product_name);
        ratingBar = findViewById(R.id.rating_bar);
        reviewHeadline = findViewById(R.id.review_headline);
        reviewComment = findViewById(R.id.review_comment);
        submitReviewButton = findViewById(R.id.submit_review);
        imgThumb = findViewById(R.id.imgThumb);

        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });

        productName.setText(shoeInfo.getTitle());
        String imageUrl = shoeInfo.getThumbnail();
        Glide.with(this)
                .load(imageUrl)
                .into(imgThumb);
    }

    private void submitReview() {
        float rating = ratingBar.getRating();
        String headline = reviewHeadline.getText().toString().trim();
        String comment = reviewComment.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(this, "Please provide a rating.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (headline.isEmpty()) {
            Toast.makeText(this, "Please provide a review title.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (comment.isEmpty()) {
            Toast.makeText(this, "Please provide a comment.", Toast.LENGTH_SHORT).show();
            return;
        }

        reviewInfoDAO.addReview(new ReviewInfo(productCode, headline, comment, rating));

        Toast.makeText(this, "Review submitted successfully!", Toast.LENGTH_SHORT).show();

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
