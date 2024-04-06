package com.example.cafe4u.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe4u.R;
import com.example.cafe4u.activity.ShopDetailActivity;
import com.example.cafe4u.models.Shop;


import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    public class ShopViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageShop;
        private TextView mNameShop;
        private TextView mVoteShop;
        private TextView mDescribeShop;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageShop = itemView.findViewById(R.id.imgShop);
            mNameShop = itemView.findViewById(R.id.nameShop);
            mVoteShop= itemView.findViewById(R.id.voteShop);
            mDescribeShop = itemView.findViewById(R.id.describeShop);
        }
    }
    private Context mContext;
    private ArrayList<Shop> mList;

    public ShopAdapter(Context mContext, ArrayList<Shop> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.item_container_shop, parent, false);
        ShopViewHolder viewHolder = new ShopViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Shop cafe = mList.get(position);
        holder.mImageShop.setImageResource(cafe.getImage());
        holder.mNameShop.setText(cafe.getName());
        holder.mVoteShop.setText(String.valueOf(cafe.getVote()));
        holder.mDescribeShop.setText(cafe.getDescribe());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chuyen den gd chi tiet quan khi nhap vao muc
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                intent.putExtra("cafeImage","0");
                intent.putExtra("cafeName",cafe.getName());
                intent.putExtra("cafeVote",cafe.getVote());
                intent.putExtra("cafeDescribe",cafe.getDescribe());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}