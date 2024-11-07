package com.example.project.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

public class CheckoutFragment extends Fragment {

    private int itemCount;
    private double subTotal;

    public CheckoutFragment(int itemCount, double subTotal) {
        this.itemCount = itemCount;
        this.subTotal = subTotal;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        TextView txtItemCount = view.findViewById(R.id.txtItemCount);
        TextView txtItemTotal = view.findViewById(R.id.txtItemTotal);
        TextView txtGST = view.findViewById(R.id.txtGST);
        TextView txtPST = view.findViewById(R.id.txtPST);
        TextView txtOrderTotal = view.findViewById(R.id.txtOrderTotal);

        String itemCountString = "Items (" + itemCount + ")";
        txtItemCount.setText(itemCountString);

        String itemTotalString = "$" + String.format("%.2f", subTotal);
        txtItemTotal.setText(itemTotalString);

        double gst = subTotal * 0.05;
        String gstString = "$" + String.format("%.2f", gst);
        txtGST.setText(gstString);

        double pst = subTotal * 0.07;
        String pstString = "$" + String.format("%.2f", pst);
        txtPST.setText(pstString);

        String orderTotalString = "$" + String.format("%.2f", (subTotal + gst + pst));
        txtOrderTotal.setText(orderTotalString);

        return view;
    }
}
