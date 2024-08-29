package com.example.btl_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText userEditText, passEditText;
    TextInputLayout userInputLayout, passInputLayout;
    ImageView showHidePass;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("san_pham");

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        userEditText = findViewById(R.id.et_User);
        passEditText = findViewById(R.id.et_Pass);
        showHidePass = findViewById(R.id.iv_Show);

        Button btn_Login = findViewById(R.id.btn_Login);
        Button btn_Register = findViewById(R.id.btn_Register);

        btn_Login.setOnClickListener(view -> LoginToHomepage());

        btn_Register.setOnClickListener(view -> {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        });

        showHidePass.setOnClickListener(v -> ShowHidePass());
    }

    protected void LoginToHomepage() {
        String inputUser = userEditText.getText().toString();
        String inputPass = passEditText.getText().toString();

        checkInput();

        // Read from the database
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean loginSuccess = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.child("user").getValue(String.class);
                    String password = snapshot.child("pass").getValue(String.class);

                    if (inputUser.equals(username) && inputPass.equals(password)) {
                        loginSuccess = true;
                        break;
                    }
                }

                if (loginSuccess) {
                    // Tài khoản hợp lệ - chuyển sang màn hình chính
                    Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Homepage.class);
                    startActivity(intent);
                } else {
                    // Tài khoản không hợp lệ
                    Toast.makeText(Login.this, "Tài khoản hoặc mật khẩu không đúng!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
                Toast.makeText(Login.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void ShowHidePass() {
        if (passEditText.getInputType() == 129) {
            passEditText.setInputType(1);
            showHidePass.setImageResource(R.drawable.ic_hide);
        }
        else {
            passEditText.setInputType(129);
            showHidePass.setImageResource(R.drawable.ic_show);
        }
    }

    protected void checkInput() {
        String inputUser = userEditText.getText().toString();
        String inputPass = passEditText.getText().toString();

        if (inputUser.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
        }

    }
}
