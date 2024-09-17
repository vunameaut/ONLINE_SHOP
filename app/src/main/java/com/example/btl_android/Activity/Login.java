package com.example.btl_android.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Activity.admin.MainAdmin;
import com.example.btl_android.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    EditText userEditText, passEditText;
    TextInputLayout userInputLayout, passInputLayout;
    ImageView showHidePass;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        userEditText = findViewById(R.id.et_User);
        passEditText = findViewById(R.id.et_Pass);
        showHidePass = findViewById(R.id.iv_Show);

        Button btn_Login = findViewById(R.id.btn_Login);
        Button btn_Register = findViewById(R.id.btn_Register);

        // Kiểm tra xem người dùng đã đăng nhập chưa
        checkLoginStatus();

        btn_Login.setOnClickListener(view -> LoginToHomepage());

        btn_Register.setOnClickListener(view -> {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        });

        showHidePass.setOnClickListener(v -> ShowHidePass());
    }

    // Hàm kiểm tra trạng thái đăng nhập
    private void checkLoginStatus() {
        FirebaseUser currentUser = auth.getCurrentUser();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedUid = sharedPreferences.getString("uid", null);

        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Kiểm tra nếu uid trong FirebaseAuth khác với uid lưu trong SharedPreferences
            if (!uid.equals(savedUid)) {
                // Cập nhật uid mới vào SharedPreferences nếu khác
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("uid", uid);
                editor.apply();
            }

            // Người dùng đã đăng nhập - kiểm tra vai trò trong Firebase
            checkUserRole(uid);
        } else if (savedUid != null) {
            // Trường hợp đã lưu uid nhưng người dùng chưa đăng nhập
            Toast.makeText(Login.this, "Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("uid");  // Xóa uid không hợp lệ
            editor.apply();
        }
    }

    protected void LoginToHomepage() {
        String inputEmail = userEditText.getText().toString();
        String inputPass = passEditText.getText().toString();

        checkInput(inputEmail, inputPass);

        auth.signInWithEmailAndPassword(inputEmail, inputPass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid(); // Lấy uid của người dùng

                            // Lưu uid vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("uid", uid);
                            editor.apply();

                            // Kiểm tra vai trò của người dùng
                            checkUserRole(uid);
                        }
                    } else {
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        String errorMessage = "Lỗi đăng nhập: " + errorCode;
                        Toast.makeText(Login.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole(String uid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("taikhoan").child(uid);

        userRef.child("role").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String role = task.getResult().getValue(String.class);
                if (role != null) {
                    if ("admin".equals(role)) {
                        // Nếu là admin, không cần kiểm tra xác thực email
                        Toast.makeText(Login.this, "Đăng nhập thành công! Vai trò: Admin", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainAdmin.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        finish(); // Kết thúc Login activity
                    } else {
                        // Người dùng không phải admin, kiểm tra xác thực email
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Homepage.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                            finish(); // Kết thúc Login activity
                        } else {
                            Toast.makeText(Login.this, "Vui lòng xác thực email trước khi đăng nhập.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Toast.makeText(Login.this, "Không thể lấy thông tin role", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Hàm hiện/ẩn mật khẩu
    protected void ShowHidePass() {
        if (passEditText.getInputType() == 129) {
            passEditText.setInputType(1);
            showHidePass.setImageResource(R.drawable.ic_hide);
        } else {
            passEditText.setInputType(129);
            showHidePass.setImageResource(R.drawable.ic_show);
        }
    }

    // Hàm kiểm tra input
    protected void checkInput(String inputEmail, String inputPass) {
        if (inputEmail.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
