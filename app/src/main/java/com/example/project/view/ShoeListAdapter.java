package com.example.project.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.project.R;
import com.example.project.model.Shoe;

import java.util.List;

import com.bumptech.glide.Glide;

public class ShoeListAdapter extends RecyclerView.Adapter<ShoeListAdapter.ViewHolder> {

    private final Context context;
    private List<Shoe> shoeList;
    private final OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView txtTitle, txtPrice, txtReviewCount;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.imgThumbnail);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            txtReviewCount = itemView.findViewById(R.id.txtReviewCount);
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
                .inflate(R.layout.grid_item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Shoe shoe = shoeList.get(position);

        holder.txtTitle.setText(shoe.getTitle());
        String price = "$" + shoe.getPrice();
        holder.txtPrice.setText(price);

        float totalRating = (float) shoe.getTotalRating();

        Log.d("ShoeListAdapter", "rating: " + totalRating);
        if (totalRating == 0) {
            holder.ratingBar.setVisibility(View.INVISIBLE);
        }
        else {
            float rating = Float.parseFloat(String.format("%.1f", totalRating / shoe.getReviewCount()));
            holder.ratingBar.setRating(rating);
            holder.ratingBar.setVisibility(View.VISIBLE);
        }

        if (shoe.getReviewCount() > 0) {
            holder.txtReviewCount.setText("(" + shoe.getReviewCount() + ")");
            holder.txtReviewCount.setVisibility(View.VISIBLE);
        }
        else {
            holder.txtReviewCount.setVisibility(View.INVISIBLE);
        }


        String imageUrl = shoe.getThumbnail();
        int cropHeightDp = 10;
        int cropHeightPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, cropHeightDp, context.getResources().getDisplayMetrics());

        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap croppedBitmap = cropTop(resource, cropHeightPx);
                        holder.thumbnail.setImageBitmap(croppedBitmap);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        holder.bind(shoe, listener);
    }

    @Override
    public int getItemCount() {
        return shoeList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Shoe> newShoeList) {
        shoeList = newShoeList;
        notifyDataSetChanged();
    }

    private Bitmap cropTop(Bitmap bitmap, int cropHeight) {
        if (cropHeight >= bitmap.getHeight()) {
            return bitmap;
        }

        return Bitmap.createBitmap(bitmap, 0, cropHeight, bitmap.getWidth(), bitmap.getHeight() - cropHeight);
    }
}
