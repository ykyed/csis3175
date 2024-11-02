package com.example.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.Shoe;
import com.example.project.model.ShoeDAO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShoeListAdapter shoeListAdapter;
    private ShoeDAO shoeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        shoeDAO = new ShoeDAO(this);

        initActionbarLayout();
        initLayout();
    }

    private void initLayout() {

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        int verticalSpacing = (int) getResources().getDimension(R.dimen.vertical_spacing);
        int horizontalSpacing = (int) getResources().getDimension(R.dimen.horizontal_spacing);
        recyclerView.addItemDecoration(new SpacingItemDecoration(verticalSpacing, horizontalSpacing));

        ArrayList<Shoe> shoeList = shoeDAO.getAllShoes();
        shoeListAdapter = new ShoeListAdapter(this, shoeList, new ShoeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Shoe shoe) {
                Intent intent = new Intent(MainActivity.this, DetailedInfoActivity.class);
                intent.putExtra(getString(R.string.key_product_code), shoe.getProductCode());

                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        recyclerView.setAdapter(shoeListAdapter);
    }

    private void initActionbarLayout() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton imgLogin = findViewById(R.id.imgLogin);
        ImageButton imgCart = findViewById(R.id.imgCart);

        imgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black, null));
    }
}