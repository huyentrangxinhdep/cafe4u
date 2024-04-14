package com.example.cafe4u.activity;

import static com.example.cafe4u.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cafe4u.R;
import com.example.cafe4u.adapters.UserAdapter;
import com.example.cafe4u.databinding.ActivityUsersBinding;
import com.example.cafe4u.listeners.UserListener;
import com.example.cafe4u.models.User;
import com.example.cafe4u.ultility.Constants;
import com.example.cafe4u.ultility.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListener {
    private ActivityUsersBinding binding;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        getUsers();
        setListener();
    }

    private void setListener() {
        binding.btnQuaylai.setOnClickListener(v -> {
            finish();
        });
    }


    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            //user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            // Kiểm tra xem trường image có giá trị không
                            String image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            if (image != null && !image.isEmpty()) {
                                user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE); // Sử dụng hình ảnh từ dữ liệu đã lưu
                            } else {
                                // Sử dụng hình ảnh mặc định từ thư mục drawable

                                user.image = "drawable.account_circle_custom"; // Thay "default_image" bằng tên hình ảnh mặc định trong thư mục drawable
                            }
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            UserAdapter userAdapter = new UserAdapter(users, this);
                            binding.usersRecycleView.setAdapter(userAdapter);
                            binding.usersRecycleView.setVisibility(View.VISIBLE);
                        }
                        else {
                            showErrorMessage();
                        }
                    }
                    else {
                        showErrorMessage();
                    }
                });
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}