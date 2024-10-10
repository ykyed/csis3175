package com.example.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.model.DBHelper;
import com.example.project.model.Shoe;
import com.example.project.model.ShoeDAO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private ShoeListAdapter shoeListAdapter;
    private DBHelper dbHelper;
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

        dbHelper = DBHelper.getInstance(this);
        shoeDAO = new ShoeDAO(this);

        initLayout();
    }

    private void initLayout() {

        gridView = findViewById(R.id.gridView);

        ArrayList<Shoe> shoeList = shoeDAO.getAllShoes();
        shoeListAdapter = new ShoeListAdapter(this, shoeList);
        gridView.setAdapter(shoeListAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Shoe selectedItem = shoeList.get(position);

                Intent intent = new Intent(MainActivity.this, DetailedInfoActivity.class);
                intent.putExtra("productCode", selectedItem.getProductCode());
                startActivity(intent);
            }
        });
    }
}