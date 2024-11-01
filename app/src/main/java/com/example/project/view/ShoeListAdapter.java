package com.example.project.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.Shoe;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;

public class ShoeListAdapter extends RecyclerView.Adapter<ShoeListAdapter.ViewHolder> {

    private Context context;
    private List<Shoe> shoeList;
    private OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView txtTitle, txtPrice;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.imgThumbnail);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        public void bind(final Shoe shoe, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(shoe);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Shoe shoe);
    }

    public ShoeListAdapter(Context context, List<Shoe> shoeList, OnItemClickListener listener) {
        this.context = context;
        this.shoeList = shoeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Shoe shoe = shoeList.get(position);

        holder.txtTitle.setText(shoe.getTitle());
        String price = "$" + shoe.getPrice();
        holder.txtPrice.setText(price);

        float rating = (float) shoe.getRating();
        Log.d("ShoeListAdapter", "rating: " + rating);
        if (rating == 0) {
            holder.ratingBar.setVisibility(View.INVISIBLE);
        }
        else {
            holder.ratingBar.setRating((float) shoe.getRating());
            holder.ratingBar.setVisibility(View.VISIBLE);
        }

        String imageUrl = shoe.getThumbnail();
        Glide.with(context)
                .load(imageUrl)
                .into(holder.thumbnail);

        holder.bind(shoe, listener);
    }

    @Override
    public int getItemCount() {
        return shoeList.size();
    }
}
