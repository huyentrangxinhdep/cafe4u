package com.example.cafe4u.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cafe4u.databinding.DangKyBinding;
import com.example.cafe4u.ultility.Constants;
import com.example.cafe4u.ultility.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.cafe4u.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class dang_ky extends AppCompatActivity {
    private DangKyBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DangKyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListener();

        //

    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    binding.imageProfile.setImageBitmap(bitmap);
                    binding.textAddImageProfile.setVisibility(View.GONE);
                    encodedImage = encodeImage(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
        else {
            // Hiển thị hình ảnh mặc định nếu người dùng không chọn hình ảnh
            binding.imageProfile.setImageResource(R.drawable.account_circle_custom);
            encodedImage = ""; // hoặc null tùy vào cách xử lý của bạn
        }
    });
    private String encodeImage (Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.btnRegister.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnRegister.setVisibility(View.VISIBLE);
        }
    }

    private void checkEmailExistence() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String email = binding.edtEmail.getText().toString().trim();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        // Email đã tồn tại trong cơ sở dữ liệu
//                        showToast("This email is already registered");
                    } else {
                        // Email chưa tồn tại trong cơ sở dữ liệu, tiến hành tạo tài khoản mới
                        signUp();
                    }
                });
    }


    private void signUp() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, binding.edtNameUser.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.edtEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.edtPasswd.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.edtNameUser.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    Intent i = new Intent(getApplicationContext(), home1.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());

                });
    }

    private Boolean isValidSignUpDetail() {
//        if (encodedImage == null) {
//            showToast("Select image");
//            return false;
//        }

        if (binding.edtNameUser.getText().toString().trim().isEmpty()) {
            showToast("Enter name");
            return false;
        }
        else if (binding.edtEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        }
        else if (binding.edtPasswd.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        }
        else if (binding.edtRewritePasswd.getText().toString().trim().isEmpty()) {
            showToast("Confirm your password");
            return false;
        }
        else if (!binding.edtPasswd.getText().toString().equals(binding.edtRewritePasswd.getText().toString())) {
            showToast("Password & confirm password must be same");
            return false;
        }
        else {
            checkEmailExistence();
            return true;
        }
    }

    private void showToast(String notice) {
        Toast.makeText(getApplicationContext(), notice, Toast.LENGTH_SHORT).show();
    }

    private void setListener() {
        binding.btnCancel.setOnClickListener(v -> {
            finish();
        });
        binding.btnRegister.setOnClickListener(v -> {
            if (isValidSignUpDetail()) {
                checkEmailExistence();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

    }

}