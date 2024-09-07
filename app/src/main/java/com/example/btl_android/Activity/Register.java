package com.example.btl_android.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Activity.Settings.DieuKhoan;
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

    public void RegisterToLogin() {
        String inputUser = userEditText.getText().toString();
        String inputEmail = emailEditText.getText().toString();
        String inputPass = passEditText.getText().toString();
        String inputCPass = cPassEditText.getText().toString();

        if (!checkInput(inputUser, inputEmail, inputPass, inputCPass)) {
            return;  // Dừng lại nếu thông tin đầu vào không hợp lệ
        }


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
                                            userRef.child("diachi").setValue(""); // Địa chỉ mặc định
                                            userRef.child("sdt").setValue(""); // Số điện thoại mặc định
                                            userRef.child("dieukhoan").setValue(false); // Đặt giá trị mặc định là false


                                            Intent intent = new Intent(this, DieuKhoan.class);
                                            intent.putExtra("intent_uid", userId);
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
        if (passEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            cPassEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            showHidePass.setImageResource(R.drawable.ic_hide);
        } else {
            passEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            cPassEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showHidePass.setImageResource(R.drawable.ic_show);
        }
        // Move the cursor to the end of the text
        passEditText.setSelection(passEditText.length());
        cPassEditText.setSelection(cPassEditText.length());
    }

    protected boolean checkInput(String inputUser, String inputEmail, String inputPass, String inputCPass) {
        if (inputUser.isEmpty()) {
            Toast.makeText(Register.this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            userEditText.requestFocus();
            return false;
        }

        if (inputEmail.isEmpty()) {
            Toast.makeText(Register.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            emailEditText.requestFocus();
            return false;
        }

        if (inputPass.isEmpty()) {
            Toast.makeText(Register.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            passEditText.requestFocus();
            return false;
        }

        if (!inputPass.equals(inputCPass)) {
            Toast.makeText(Register.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
