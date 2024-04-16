package com.example.cafe4u.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cafe4u.R;
import com.example.cafe4u.activity.chitiet_quan;
import com.example.cafe4u.models.Shop;


import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    Context context;
    ArrayList<Shop> list;

    public ShopAdapter(Context context, ArrayList<Shop> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_container_shop, parent, false);
        return new ShopViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {

        Shop shop = list.get(position);
        holder.name.setText(shop.getName());
        holder.openTime.setText(shop.getOpenTime());
        holder.address.setText(shop.getAddress());
        holder.vote.setRating(shop.getVote());
        // Lấy URL của hình ảnh từ đối tượng Shop
        String imageUrl = shop.getImageShop();

        // Sử dụng Glide để tải hình ảnh từ URL và hiển thị nó trong ImageView
        Glide.with(context)
                .load(imageUrl)
                .into(holder.imageShop);
        holder.itemView.setOnClickListener(v -> {
            // Tạo và khởi chạy Intent
            Intent intent = new Intent(context, chitiet_quan.class);
            intent.putExtra("shopId", shop.getShopId());  // Đưa shopId vào Intent
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        TextView name, openTime, address;
        ImageView imageShop;
        RatingBar vote;
        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameShop);
            openTime = itemView.findViewById(R.id.openTimeShop);
            address = itemView.findViewById(R.id.addressShop);
            vote = itemView.findViewById(R.id.ratingBarShop);
            imageShop = itemView.findViewById(R.id.imgShop);
        }
    }
}