package com.example.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.Shoe;
import com.example.project.model.ShoeDAO;
import com.example.project.viewmodel.FilterViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements FilterFragment.FilterListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ShoeListAdapter shoeListAdapter;
    private ShoeDAO shoeDAO;
    private FilterViewModel filterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
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

        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
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
        });qqqq
        recyclerView.setAdapter(shoeListAdapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterFragment filterFragment = new FilterFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, filterFragment)
                        .addToBackStack(null)
                        .commit();

                fab.setVisibility(View.GONE);
            }
        });

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDetached(fm, f);
                if (fab.getVisibility() != View.VISIBLE) {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        }, true);
    }

    @Override
    public void onFilterApplied(Set<String> brands, Set<String> colors, Set<String> styles) {
        List<Shoe> filteredShoes = shoeDAO.filterShoes(brands, colors, styles);
        shoeListAdapter.updateData(filteredShoes);
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