package com.example.project.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.model.CartInfo;
import com.example.project.model.CartInfoDAO;
import com.example.project.model.Shoe;
import com.example.project.model.ShoeDAO;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private final Context context;
    private List<CartInfo> cartInfoList;
    private final CartListAdapter.OnItemClickListener listener;
    private final ShoeDAO shoeDAO;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView txtTitle, txtPrice, txtSize, txtQuantity;
        public ImageButton btnIncrease, btnDecrease;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.imgThumb);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtSize = itemView.findViewById(R.id.txtSize);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }

        public void bind(final Shoe shoe, CartInfo cartInfo, int position, final CartListAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(shoe);
                }
            });
            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartInfo.setQuantity(cartInfo.getQuantity() + 1);
                    txtQuantity.setText(String.valueOf(cartInfo.getQuantity()));

                    listener.onIncreaseBtnClick(cartInfo);
                }
            });
            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartInfo.setQuantity(cartInfo.getQuantity() - 1);
                    txtQuantity.setText(String.valueOf(cartInfo.getQuantity()));

                    listener.onDecreaseBtnClick(cartInfo, getAdapterPosition());
                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCartList(List<CartInfo> cartList) {
        this.cartInfoList = cartList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Shoe shoe);
        void onIncreaseBtnClick(CartInfo cartInfo);
        void onDecreaseBtnClick(CartInfo cartInfo, int position);

    }

    public CartListAdapter(Context context, List<CartInfo> cartInfoList, ShoeDAO shoeDAO, CartListAdapter.OnItemClickListener listener) {
        this.context = context;
        this.cartInfoList = cartInfoList;
        this.listener = listener;
        this.shoeDAO = shoeDAO;
    }

    @NonNull
    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cart, parent, false);
        return new CartListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {

        CartInfo cartInfo = cartInfoList.get(position);
        Shoe shoe = shoeDAO.getShoe(cartInfo.getProductCode());

        holder.txtTitle.setText(shoe.getTitle());
        String price = "$" + String.format("%.2f", shoe.getPrice());
        holder.txtPrice.setText(price);

        holder.txtSize.setText("Size: " + cartInfo.getSize());
        holder.txtQuantity.setText(String.valueOf(cartInfo.getQuantity()));

        String imageUrl = shoe.getThumbnail();
        Glide.with(context)
                .load(imageUrl)
                .into(holder.thumbnail);

        holder.bind(shoe, cartInfo, holder.getAdapterPosition(), listener);
    }

    @Override
    public int getItemCount() {
        return cartInfoList.size();
    }
}
