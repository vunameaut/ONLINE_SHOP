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

import android.text.Editable;
import android.text.TextWatcher;

public class AdminAccountDetailActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPhone, editTextAddress, editTextRole;
    private TextView textViewUid;
    private Button btnUpdate, btnDelete;
    private ImageButton btnBack;
    private DatabaseReference databaseReference;
    private String uid;

    // Chuỗi cố định cho các trường
    private static final String USERNAME_PREFIX = "Tên người dùng: ";
    private static final String EMAIL_PREFIX = "Email: ";
    private static final String PHONE_PREFIX = "Số điện thoại: ";
    private static final String ADDRESS_PREFIX = "Địa chỉ: ";
    private static final String ROLE_PREFIX = "Vai trò: ";

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
        editTextUsername.setText(USERNAME_PREFIX + username);
        editTextEmail.setText(EMAIL_PREFIX + email);
        editTextPhone.setText(PHONE_PREFIX + phone);
        editTextAddress.setText(ADDRESS_PREFIX + address);
        editTextRole.setText(ROLE_PREFIX + role);
        textViewUid.setText("UID: " + uid);

        // Khóa phần trước dấu hai chấm trong EditText
        lockPrefixInEditText(editTextUsername, USERNAME_PREFIX);
        lockPrefixInEditText(editTextEmail, EMAIL_PREFIX);
        lockPrefixInEditText(editTextPhone, PHONE_PREFIX);
        lockPrefixInEditText(editTextAddress, ADDRESS_PREFIX);
        lockPrefixInEditText(editTextRole, ROLE_PREFIX);

        // Khởi tạo DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("taikhoan").child(uid);

        // Xử lý sự kiện nhấn nút Update
        btnUpdate.setOnClickListener(v -> updateAccount());

        // Xử lý sự kiện nhấn nút Delete
        btnDelete.setOnClickListener(v -> deleteAccount());

        // Xử lý sự kiện nhấn nút Back
        btnBack.setOnClickListener(v -> finish());
    }

    // Phương thức khóa phần trước dấu hai chấm
    private void lockPrefixInEditText(EditText editText, String prefix) {
        editText.addTextChangedListener(new TextWatcher() {
            boolean isEditing;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;

                // Kiểm tra nếu phần đầu không khớp với prefix thì đặt lại
                if (!s.toString().startsWith(prefix)) {
                    editText.setText(prefix);
                    editText.setSelection(prefix.length());  // Di chuyển con trỏ sau prefix
                } else {
                    // Ngăn người dùng chỉnh sửa phần prefix
                    if (s.length() < prefix.length()) {
                        editText.setText(prefix);
                        editText.setSelection(prefix.length());
                    }
                }

                isEditing = false;
            }
        });
    }

    // Phương thức để chỉ cập nhật phần sau dấu hai chấm
    private void updateAccount() {
        String updatedUsername = extractData(editTextUsername.getText().toString().trim(), USERNAME_PREFIX);
        String updatedEmail = extractData(editTextEmail.getText().toString().trim(), EMAIL_PREFIX);
        String phoneString = extractData(editTextPhone.getText().toString().trim(), PHONE_PREFIX);

        // Xóa mọi ký tự không phải số nếu cần
        String numericPhoneString = phoneString.replaceAll("[^0-9]", "");

        Long updatedPhone = phoneString.isEmpty() ? null : Long.valueOf(numericPhoneString);
        String updatedAddress = extractData(editTextAddress.getText().toString().trim(), ADDRESS_PREFIX);
        String updatedRole = extractData(editTextRole.getText().toString().trim(), ROLE_PREFIX);

        // Cập nhật dữ liệu vào Firebase
        databaseReference.child("username").setValue(updatedUsername);
        databaseReference.child("email").setValue(updatedEmail);
        databaseReference.child("sdt").setValue(updatedPhone);
        databaseReference.child("diachi").setValue(updatedAddress);
        databaseReference.child("role").setValue(updatedRole)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminAccountDetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AdminAccountDetailActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteAccount() {
        // Xóa tài khoản khỏi Firebase
        databaseReference.removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminAccountDetailActivity.this, "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AdminAccountDetailActivity.this, "Xóa tài khoản thất bại", Toast.LENGTH_SHORT).show();
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
