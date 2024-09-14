package com.example.btl_android.Activity.admin.taikhoan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountDetailActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPhone, editTextAddress, editTextRole;
    private TextView textViewUid;
    private Button btnUpdate, btnDelete;
    private ImageButton btnBack;
    private DatabaseReference databaseReference;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        // Liên kết các view
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextRole = findViewById(R.id.editTextRole);
        textViewUid = findViewById(R.id.TextViewUid);
        btnUpdate = findViewById(R.id.btnSua);
        btnDelete = findViewById(R.id.btnXoa);
        btnBack = findViewById(R.id.btnBack);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        String role = intent.getStringExtra("role");
        uid = intent.getStringExtra("uid");

        // Hiển thị thông tin với nhãn
        editTextUsername.setText("Tên người dùng: " + username);
        editTextEmail.setText("Email: " + email);
        editTextPhone.setText("Số điện thoại: " + phone);
        editTextAddress.setText("Địa chỉ: " + address);
        editTextRole.setText("Vai trò: " + role);
        textViewUid.setText("UID: " + uid);

        // Khởi tạo DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("taikhoan").child(uid);

        // Xử lý sự kiện nhấn nút Update
        btnUpdate.setOnClickListener(v -> updateAccount());

        // Xử lý sự kiện nhấn nút Delete
        btnDelete.setOnClickListener(v -> deleteAccount());

        // Xử lý sự kiện nhấn nút Back
        btnBack.setOnClickListener(v -> finish());
    }

    private void updateAccount() {
        String updatedUsername = extractData(editTextUsername.getText().toString().trim(), "Tên người dùng: ");
        String updatedEmail = extractData(editTextEmail.getText().toString().trim(), "Email: ");
        String phoneString = extractData(editTextPhone.getText().toString().trim(), "Số điện thoại: ");

        // Xóa mọi ký tự không phải số nếu cần
        String numericPhoneString = phoneString.replaceAll("[^0-9]", "");

        Long updatedPhone = phoneString.isEmpty() ? null : Long.valueOf(numericPhoneString);
        String updatedAddress = extractData(editTextAddress.getText().toString().trim(), "Địa chỉ: ");
        String updatedRole = extractData(editTextRole.getText().toString().trim(), "Vai trò: ");

        // Cập nhật dữ liệu vào Firebase
        databaseReference.child("username").setValue(updatedUsername);
        databaseReference.child("email").setValue(updatedEmail);
        databaseReference.child("sdt").setValue(updatedPhone);
        databaseReference.child("diachi").setValue(updatedAddress);
        databaseReference.child("role").setValue(updatedRole)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AccountDetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AccountDetailActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteAccount() {
        // Xóa tài khoản khỏi Firebase
        databaseReference.removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AccountDetailActivity.this, "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AccountDetailActivity.this, "Xóa tài khoản thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String extractData(String text, String label) {
        if (text.startsWith(label)) {
            return text.substring(label.length()).trim();
        }
        return text;
    }
}
