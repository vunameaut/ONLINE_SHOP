package com.example.btl_android.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;

public class Login extends AppCompatActivity {

    EditText userEditText, passEditText;
    TextInputLayout userInputLayout, passInputLayout;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        userEditText = findViewById(R.id.et_User);
        passEditText = findViewById(R.id.et_Pass);

        Button btn_Login = findViewById(R.id.btn_Login);
        Button btn_Register = findViewById(R.id.btn_Register);

        // Kiểm tra xem người dùng đã đăng nhập chưa
        checkLoginStatus();

        btn_Login.setOnClickListener(view -> LoginToHomepage());

        btn_Register.setOnClickListener(view -> {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        });

    }

    // Hàm kiểm tra trạng thái đăng nhập
    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", null);
        if (uid != null) {
            // Người dùng đã đăng nhập - chuyển đến màn hình chính
            Intent intent = new Intent(Login.this, Homepage.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
            finish();  // Đóng màn hình đăng nhập để người dùng không quay lại màn hình này nữa
        }
    }

    // Hàm đăng nhập
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

                            if (user.isEmailVerified()) {
                                // Email đã được xác thực - chuyển sang màn hình chính
                                Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, Homepage.class);
                                intent.putExtra("uid", uid); // Truyền uid qua Intent nếu cần

                                startActivity(intent);
                                finish(); // Đóng màn hình đăng nhập
                            } else {
                                Toast.makeText(Login.this, "Vui lòng xác thực email trước khi đăng nhập.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        // Nếu đăng nhập thất bại
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        String errorMessage = "Lỗi đăng nhập: " + errorCode;
                        Toast.makeText(Login.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
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
