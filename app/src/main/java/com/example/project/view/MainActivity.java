package com.example.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.Shoe;
import com.example.project.model.ShoeDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends ToolbarBaseActivity implements FilterFragment.FilterListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ShoeListAdapter shoeListAdapter;
    private ShoeDAO shoeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        FrameLayout contentFrame = findViewById(R.id.contentFrame);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, contentFrame, false);
        contentFrame.addView(contentView);

        shoeDAO = new ShoeDAO(this);

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
                setBackButtonVisibility(View.VISIBLE);
            }
        });

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDetached(fm, f);
                if (fab.getVisibility() != View.VISIBLE) {
                    fab.setVisibility(View.VISIBLE);
                }
                setBackButtonVisibility(View.INVISIBLE);
            }
        }, true);

        setBackButtonVisibility(View.INVISIBLE);
    }

    @Override
    public void onFilterApplied(Set<String> brands, Set<String> colors, Set<String> styles) {
        List<Shoe> filteredShoes = shoeDAO.filterShoes(brands, colors, styles);
        shoeListAdapter.updateData(filteredShoes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shoeListAdapter.updateData(shoeDAO.getAllShoes());
    }
}