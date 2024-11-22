package com.example.project.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project.R;
import com.example.project.model.CartInfo;
import com.example.project.model.CartInfoDAO;
import com.example.project.model.ReviewInfo;
import com.example.project.model.ReviewInfoDAO;
import com.example.project.model.Shoe;
import com.example.project.model.ShoeDAO;
import com.example.project.model.SizeInfo;
import com.example.project.model.SizeInfoDAO;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class DetailedInfoActivity extends ToolbarBaseActivity {

    private TextView txtProductName, txtProductPrice, txtStarRate, txtReviewTitle;
    private RatingBar ratingBar;
    private Button btnCart, selectedButton, btnReview;
    private GridLayout sizeButtonGrid;
    private ImageButton btnToggle;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ScrollView scrollView;
    private LinearLayout layoutReview;

    private CartInfoDAO cartInfoDAO;
    private ShoeDAO shoeDAO;
    private ReviewInfoDAO reviewInfoDAO;
    private SizeInfoDAO sizeInfoDAO;

    private String productCode;
    private Shoe shoeInfo;
    private boolean areReviewsVisible = false;

    private ArrayList<SizeInfo> sizeInfos;
    private RecyclerView recyclerView;
    private ReviewListAdapter reviewListAdapter;

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
        sizeInfoDAO = new SizeInfoDAO(this);

        productCode = getIntent().getStringExtra(getString(R.string.key_product_code));
        shoeInfo = shoeDAO.getShoe(productCode);
        sizeInfos = (ArrayList<SizeInfo>)sizeInfoDAO.getSizesByProductCode(productCode);

        initLayout();
    }

    private void initLayout() {
        scrollView = findViewById(R.id.scrollView);
        txtStarRate = findViewById(R.id.txtStarRate);
        ratingBar = findViewById(R.id.ratingBar);
        txtProductName = findViewById(R.id.txtProductName);
        txtProductPrice = findViewById(R.id.txtProductPrice);

        btnCart = findViewById(R.id.btnCart);
        btnCart.setEnabled(false);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = getUserEmail();

                if (!userEmail.isEmpty()) {
                    CartInfo cartInfo = new CartInfo();
                    cartInfo.setProductCode(shoeInfo.getProductCode());
                    cartInfo.setEmail(userEmail);
                    cartInfo.setSize(Double.parseDouble(selectedButton.getText().toString().replace("US ", "")));
                    cartInfo.setQuantity(1);

                    cartInfoDAO.addItem(cartInfo);
                    updateToolbar(getUserName(), userEmail);

                } else {
                    Intent intent = new Intent(DetailedInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        sizeButtonGrid = findViewById(R.id.sizeButtonGrid);
        createSizeButtons();

        btnToggle = findViewById(R.id.btnToggle);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        reviewListAdapter = new ReviewListAdapter(reviewInfoDAO.getReviewsByPrductCode(productCode));
        recyclerView.setAdapter(reviewListAdapter);

        recyclerView.setVisibility(View.GONE);

        layoutReview = findViewById(R.id.layoutReview);
        txtReviewTitle = findViewById(R.id.txtReviewTitle);
        txtReviewTitle.setText("Reviews" + " (" + shoeInfo.getReviewCount() + ")");

        layoutReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleReviewsVisibility();
            }
        });

        txtProductName.setText(shoeInfo.getTitle());
        txtProductPrice.setText("$ " + String.format("%.2f", shoeInfo.getPrice()));

        btnReview = findViewById(R.id.btnReview);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!getUserEmail().isEmpty()) {
                    Intent intent = new Intent(DetailedInfoActivity.this, ReviewActivity.class);
                    intent.putExtra("product_code", shoeInfo.getProductCode());
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                } else {
                    Intent intent = new Intent(DetailedInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        loadImages();
        setBackButtonVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void toggleReviewsVisibility() {
        if (areReviewsVisible) {
            btnReview.setVisibility(View.INVISIBLE);
            btnToggle.setImageResource(R.drawable.download);
            recyclerView.setVisibility(View.GONE);
        } else {
            btnReview.setVisibility(View.VISIBLE);
            btnToggle.setImageResource(R.drawable.upload);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.post(() -> scrollView.smoothScrollBy(0, 500));

        }
        areReviewsVisible = !areReviewsVisible;
    }

    private void createSizeButtons() {
        int totalButtons = sizeInfos.size();
        for (int i = 0; i < totalButtons; i++) {
            Button sizeButton = createSizeButton( "US " + sizeInfos.get(i).getSize(), sizeInfos.get(i).getQuantity());
            sizeButtonGrid.addView(sizeButton);
        }
    }

    private Button createSizeButton(String size, int quantity) {

        Button sizeButton = new Button(this);
        sizeButton.setText(size);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = dpToPx(this, 62);
        params.height = dpToPx(this, 40);

        params.setMargins(dpToPx(this, 4), dpToPx(this, 4), dpToPx(this, 4), dpToPx(this, 4));
        sizeButton.setLayoutParams(params);

        sizeButton.setTextSize(13);

        if(quantity == 0) {
            sizeButton.setEnabled(false);
            sizeButton.setBackgroundResource(R.drawable.size_button_disable);
            sizeButton.setTextColor(ContextCompat.getColorStateList(this, R.color.white));
        }
        else {
            sizeButton.setEnabled(true);
            sizeButton.setBackgroundResource(R.drawable.size_button_uncheck);
            sizeButton.setTextColor(ContextCompat.getColorStateList(this, R.color.black));
        }

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
            selectedButton.setBackgroundResource(R.drawable.size_button_uncheck);
        }
        clickedButton.setBackgroundResource(R.drawable.size_button_check);
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

    private int dpToPx(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    private String getUserEmail() {
        SharedPreferences sh = getSharedPreferences(getResources().getString(R.string.user_info_shared_preference), MODE_PRIVATE);
        return sh.getString(getResources().getString(R.string.key_email), "");
    }

    private String getUserName() {
        SharedPreferences sh = getSharedPreferences(getResources().getString(R.string.user_info_shared_preference), MODE_PRIVATE);
        return sh.getString(getResources().getString(R.string.key_first_name), "");
    }

    private View createTabView(boolean isSelected) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
        ImageView dot = view.findViewById(R.id.dot);
        dot.setImageResource(isSelected ? R.drawable.selected_dot : R.drawable.unselected_dot);
        return view;
    }

    private void loadImages() {
        try {
            String storedJson = shoeInfo.getImages();
            JSONArray restoredJsonArray = new JSONArray(storedJson);

            List<String> imageUrls = new ArrayList<>();

            for (int i = 0; i < restoredJsonArray.length(); i++) {
                imageUrls.add(restoredJsonArray.getString(i));
            }

            ImageAdapter adapter = new ImageAdapter(this, imageUrls);
            viewPager.setAdapter(adapter);

            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                tab.setCustomView(createTabView(position == 0));
            }).attach();

            viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    for (int i = 0; i < tabLayout.getTabCount(); i++) {
                        View customView = tabLayout.getTabAt(i).getCustomView();
                        if (customView != null) {
                            ImageView dot = customView.findViewById(R.id.dot);
                            dot.setImageResource(i == position ? R.drawable.selected_dot : R.drawable.unselected_dot);
                        }
                    }
                }
            });
        } catch (JSONException e) {
            Log.d("DetailedInfoActivity", "exception: " + e);
        }
    }

    private void updateUI() {
        Shoe newShoeInfo = shoeDAO.getShoe(productCode);

        if (shoeInfo.getReviewCount() != newShoeInfo.getReviewCount()) {
            shoeInfo = newShoeInfo;

            txtReviewTitle.setText("Reviews" + " (" + shoeInfo.getReviewCount() + ")");

            List<ReviewInfo> reviewInfo = reviewInfoDAO.getReviewsByPrductCode(productCode);
            reviewListAdapter.setReviewInfoList(reviewInfo);
            reviewListAdapter.notifyItemInserted(reviewInfo.size() - 1);
        }

        if (shoeInfo.getReviewCount() > 0) {
            ratingBar.setVisibility(View.VISIBLE);
            txtStarRate.setVisibility(View.VISIBLE);

            float rating = Float.parseFloat(String.format("%.1f", shoeInfo.getTotalRating() / shoeInfo.getReviewCount()));
            ratingBar.setRating(rating);

            txtStarRate.setText(String.valueOf(rating));
        }
        else {
            ratingBar.setVisibility(View.INVISIBLE);
            txtStarRate.setVisibility(View.INVISIBLE);
        }
    }
}