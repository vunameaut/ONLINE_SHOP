package com.example.btl_android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText userEditText, emailEditText, passEditText, cPassEditText;
    ImageView showHidePass;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("taikhoan");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

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

        checkInput();

        dbRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String username = snapshot.child("user").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    if (inputUser.equals(username)) {
                        Toast.makeText(Register.this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (inputEmail.equals(email)) {
                        Toast.makeText(Register.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long count = task.getResult().getChildrenCount() + 1;

                    String key = "t" + count;

                    dbRef.child(key).child("user").setValue(inputUser);
                    dbRef.child(key).child("pass").setValue(inputPass);
                    dbRef.child(key).child("email").setValue(inputEmail);

                    Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                }
            }
            else {
                Toast.makeText(Register.this, "Lỗi đăng ký", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void ShowHidePass() {
        if (passEditText.getInputType() == 129) {
            passEditText.setInputType(1);
            cPassEditText.setInputType(1);
            showHidePass.setImageResource(R.drawable.ic_hide);
        }
        else {
            passEditText.setInputType(129);
            cPassEditText.setInputType(129);
            showHidePass.setImageResource(R.drawable.ic_show);
        }
    }

    protected void checkInput() {
        String inputUser = userEditText.getText().toString();
        String inputEmail = emailEditText.getText().toString();
        String inputPass = passEditText.getText().toString();
        String inputCPass = cPassEditText.getText().toString();

        if (inputUser.isEmpty()) {
            Toast.makeText(Register.this,"Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            userEditText.requestFocus();

            return;
        }

        if (!inputPass.equals(inputCPass)) {
            Toast.makeText(Register.this,"Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}