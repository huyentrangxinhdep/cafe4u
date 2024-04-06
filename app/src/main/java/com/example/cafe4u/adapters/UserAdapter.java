package com.example.cafe4u.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe4u.databinding.ItemContainerUsersBinding;
import com.example.cafe4u.listeners.UserListener;
import com.example.cafe4u.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private final List<User> users;
    private final UserListener userListener;



    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUsersBinding itemContainerUsersBinding = ItemContainerUsersBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(itemContainerUsersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public UserAdapter(List<User> users, UserListener userListener) {


        this.users = users;
        this.userListener = userListener;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        ItemContainerUsersBinding binding;
        UserViewHolder(ItemContainerUsersBinding itemContainerUsersBinding) {
            super(itemContainerUsersBinding.getRoot());
            binding = itemContainerUsersBinding;
        }
        void setUserData(User user) {
            binding.textName.setText(user.name);
            
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));

        }
    }
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
