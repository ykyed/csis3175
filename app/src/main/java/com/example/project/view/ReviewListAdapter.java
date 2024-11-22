package com.example.project.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.ReviewInfo;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private List<ReviewInfo> reviewInfoList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtReviewTitle, txtReviewComment;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            txtReviewTitle = itemView.findViewById(R.id.txtReviewTitle);
            txtReviewComment = itemView.findViewById(R.id.txtReviewComment);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

    public ReviewListAdapter(List<ReviewInfo> reviewInfoList) {
        this.reviewInfoList = reviewInfoList;
    }

    @NonNull
    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_review, parent, false);
        return new ReviewListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtReviewTitle.setText(reviewInfoList.get(position).getTitle());
        holder.txtReviewComment.setText(reviewInfoList.get(position).getComment());
        holder.ratingBar.setRating((float) reviewInfoList.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return reviewInfoList.size();
    }

    public void setReviewInfoList(List<ReviewInfo> reviewInfoList) {
        this.reviewInfoList = reviewInfoList;
    }
}
