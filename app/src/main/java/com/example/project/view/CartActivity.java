package com.example.project.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

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

public class CartActivity extends ToolbarLogoBaseActivity {

    private RecyclerView recyclerView;
    private TextView txtCartTotal, txtTotalPrice;
    private CartListAdapter cartListAdapter;
    private CartInfoDAO cartInfoDAO;
    private ShoeDAO shoeDAO;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrame = findViewById(R.id.contentFrame);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_cart, contentFrame, false);
        contentFrame.addView(contentView);

        cartInfoDAO = new CartInfoDAO(this);
        shoeDAO = new ShoeDAO(this);

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


}