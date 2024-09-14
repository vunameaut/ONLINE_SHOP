package com.example.btl_android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText userEditText, emailEditText, passEditText, cPassEditText;
    ImageView showHidePass;

    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("taikhoan");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        showHidePass = findViewById(R.id.iv_Show);
        userEditText = findViewById(R.id.et_User);
        emailEditText = findViewById(R.id.et_Email);
        passEditText = findViewById(R.id.et_Pass);
        cPassEditText = findViewById(R.id.et_CPass);

        Button btn_Login = findViewById(R.id.btn_Login);
        Button btn_Register = findViewById(R.id.btn_Register);

        btn_Login.setOnClickListener(view -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });

        btn_Register.setOnClickListener(view -> RegisterToLogin());

        showHidePass.setOnClickListener(v -> ShowHidePass());
    }

    protected void RegisterToLogin() {
        String inputUser = userEditText.getText().toString();
        String inputEmail = emailEditText.getText().toString();
        String inputPass = passEditText.getText().toString();
        String inputCPass = cPassEditText.getText().toString();

        checkInput(inputUser, inputEmail, inputPass, inputCPass);

        auth.createUserWithEmailAndPassword(inputEmail, inputPass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(emailTask -> {
                                        if (emailTask.isSuccessful()) {
                                            // Lưu thông tin người dùng vào Realtime Database, sử dụng UID làm khóa ngoài
                                            String userId = user.getUid(); // Lấy UID của người dùng

                                            DatabaseReference userRef = dbRef.child(userId); // Sử dụng UID làm khóa ngoài
                                            userRef.child("uid").setValue(userId);
                                            userRef.child("username").setValue(inputUser);
                                            userRef.child("email").setValue(inputEmail);
                                            userRef.child("email").setValue("user");
                                            userRef.child("diachi").setValue(""); // Địa chỉ mặc định
                                            userRef.child("sdt").setValue(""); // Số điện thoại mặc định

                                            Toast.makeText(Register.this, "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.", Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(this, Login.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(Register.this, "Lỗi gửi email xác thực", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Nếu đăng ký thất bại
                        Toast.makeText(Register.this, "Lỗi đăng ký", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected void ShowHidePass() {
        if (passEditText.getInputType() == 129) {
            passEditText.setInputType(1);
            cPassEditText.setInputType(1);
            showHidePass.setImageResource(R.drawable.ic_hide);
        } else {
            passEditText.setInputType(129);
            cPassEditText.setInputType(129);
            showHidePass.setImageResource(R.drawable.ic_show);
        }
    }

    protected void checkInput(String inputUser, String inputEmail, String inputPass, String inputCPass) {
        if (inputUser.isEmpty()) {
            Toast.makeText(Register.this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            userEditText.requestFocus();
            return;
        }

        if (inputEmail.isEmpty()) {
            Toast.makeText(Register.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            emailEditText.requestFocus();
            return;
        }

        if (inputPass.isEmpty()) {
            Toast.makeText(Register.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            passEditText.requestFocus();
            return;
        }

        if (!inputPass.equals(inputCPass)) {
            Toast.makeText(Register.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
