package com.example.project.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private List<String> data;
    private Set<String> selectedItems = new HashSet<>();

    public FilterAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = data.get(position);
        holder.textView.setText(item);
        holder.checkBox.setChecked(selectedItems.contains(item));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                } else {
                    selectedItems.add(item);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (holder.checkBox.isChecked()) {
                   holder.checkBox.setChecked(false);
               }
               else {
                   holder.checkBox.setChecked(true);
               }

               if (selectedItems.contains(item)) {
                   selectedItems.remove(item);
               } else {
                   selectedItems.add(item);
               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Set<String> getSelectedItems() {
        return selectedItems;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectedItems(Set<String> items) {
        selectedItems.clear();
        selectedItems.addAll(items);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void resetSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}