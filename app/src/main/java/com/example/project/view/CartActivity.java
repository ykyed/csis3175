package com.example.project.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.CartInfo;
import com.example.project.model.CartInfoDAO;
import com.example.project.model.Shoe;
import com.example.project.model.ShoeDAO;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtCartTotal, txtTotalPrice;
    private CartListAdapter cartListAdapter;
    private CartInfoDAO cartInfoDAO;
    private ShoeDAO shoeDAO;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cartInfoDAO = new CartInfoDAO(this);
        shoeDAO = new ShoeDAO(this);
        initActionbarLayout();
        initLayout();
    }

    @SuppressLint("DefaultLocale")
    private void initLayout() {

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ArrayList<CartInfo> cartList = cartInfoDAO.getCartItemsByUser("wooastudio1012@gmail.com");
        cartListAdapter = new CartListAdapter(this, cartList, shoeDAO, new CartListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Shoe shoe) {}

            @Override
            public void onIncreaseBtnClick(CartInfo cartInfo) {
                cartInfoDAO.updateItem(cartInfo);
                totalPrice += shoeDAO.getShoe(cartInfo.getProductCode()).getPrice();
                changePrice(totalPrice);
            }

            @Override
            public void onDecreaseBtnClick(CartInfo cartInfo, int position) {

                totalPrice -= shoeDAO.getShoe(cartInfo.getProductCode()).getPrice();
                changePrice(totalPrice);

                if (cartInfo.getQuantity() == 0) {
                    cartInfoDAO.deleteItem(cartInfo.getId());
                    cartList.remove(position);
                    cartListAdapter.setCartList(cartList);
                }
                else {
                    cartInfoDAO.updateItem(cartInfo);
                }
            }
        });
        recyclerView.setAdapter(cartListAdapter);

        txtCartTotal = findViewById(R.id.txtCartTotal);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);

        totalPrice = 0;
        for (int i = 0; i < cartList.size(); i++) {

            double price = shoeDAO.getShoe(cartList.get(i).getProductCode()).getPrice();
            int quantity = cartList.get(i).getQuantity();
            price *= quantity;
            totalPrice += price;
        }

        changePrice(totalPrice);
    }

    private void changePrice(double price) {
        String subTotal = "$" + String.format("%.2f", price);
        txtCartTotal.setText(subTotal);
        subTotal += " + Tax";
        txtTotalPrice.setText(subTotal);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void initActionbarLayout() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black, null));
    }
}