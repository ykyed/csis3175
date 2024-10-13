package com.example.project.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.model.Shoe;

import java.util.ArrayList;

import com.bumptech.glide.Glide;

public class ShoeListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Shoe> shoeList;

    public ShoeListAdapter(Context context, ArrayList<Shoe> shoeList) {
        this.context = context;
        this.shoeList = shoeList;
    }

    @Override
    public int getCount() {
        return shoeList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }

        ImageView thumbnail = convertView.findViewById(R.id.imgThumbnail);
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);

        Shoe shoe = shoeList.get(position);

        txtTitle.setText(shoe.getTitle());

        String imageUrl = shoe.getThumbnail();
        Glide.with(context)
                .load(imageUrl)  // 이미지 URL
                .placeholder(R.drawable.placeholder)  // 이미지 로드 중에 표시할 기본 이미지
                .error(R.drawable.error_image)  // 에러 발생 시 표시할 이미지
                .into(thumbnail);

        return convertView;
    }
}