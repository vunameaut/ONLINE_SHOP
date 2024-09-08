package com.example.btl_android.Activity.Settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class myProfile extends AppCompatActivity {

    TextView viewUser;
    TextInputEditText editUser, editEmail, editPhone, editAddress;
    LinearLayout linearLayout;
    Button btnCancel, btnSave;
    ImageView btnBack;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("taikhoan");

    private boolean isDataChanged = false;
    private String currentUser, currentEmail, currentPhone, currentAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.setting_myprofile);

        Mapping();

        // Ban đầu ẩn LinearLayout
        linearLayout.setVisibility(View.GONE);

        GetData();

        WatchForChanges();

        btnSave.setOnClickListener(v -> {
            clearFocus();
            if (isDataChanged) {
                showConfirm();
            }
        });

        btnCancel.setOnClickListener(v -> {
            clearFocus();
            GetData();
        });

        btnBack.setOnClickListener(v -> {
            clearFocus();
            if (isDataChanged) {
                showConfirmBack();
            }
            else {
                Intent intent = new Intent(myProfile.this, TKvaBM.class);
                startActivity(intent);
            }
        });
    }

    private void Mapping() {
        viewUser = findViewById(R.id.tv_user);
        editUser = findViewById(R.id.et_user);
        editEmail = findViewById(R.id.et_email);
        editPhone = findViewById(R.id.et_numPhone);
        editAddress = findViewById(R.id.et_address);
        linearLayout = findViewById(R.id.ll_btn);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.ivBack);
    }

    private void GetData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", null);

        if (uid == null) {
            Toast.makeText(this, "Không thể xác thực người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                currentUser = task.getResult().child("username").getValue(String.class);
                currentEmail = task.getResult().child("email").getValue(String.class);
                currentPhone = task.getResult().child("sdt").getValue(String.class);
                currentAddress = task.getResult().child("diachi").getValue(String.class);

                viewUser.setText(currentUser);
                editUser.setText(currentUser);
                editEmail.setText(currentEmail);
                editPhone.setText(currentPhone);
                editAddress.setText(currentAddress);

            } else {
                Toast.makeText(this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", null);

        if (uid == null) {
            Toast.makeText(this, "Không thể xác thực người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validation trước khi lưu
        if (editUser.getText().toString().isEmpty() || editEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child(uid).child("username").setValue(editUser.getText().toString());
        dbRef.child(uid).child("email").setValue(editEmail.getText().toString());
        dbRef.child(uid).child("sdt").setValue(editPhone.getText().toString());
        dbRef.child(uid).child("diachi").setValue(editAddress.getText().toString());
    }

    private void WatchForChanges() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasChanges = !editUser.getText().toString().equals(currentUser) ||
                        !editEmail.getText().toString().equals(currentEmail) ||
                        !editPhone.getText().toString().equals(currentPhone) ||
                        !editAddress.getText().toString().equals(currentAddress);

                if (hasChanges) {
                    isDataChanged = true;
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    isDataChanged = false;
                    linearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        editUser.addTextChangedListener(textWatcher);
        editEmail.addTextChangedListener(textWatcher);
        editPhone.addTextChangedListener(textWatcher);
        editAddress.addTextChangedListener(textWatcher);
    }

    private void showConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Thiết lập tiêu đề và nội dung
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có chắc chắn muốn lưu?");

        // Nút "Yes"
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            SetData();
            Toast.makeText(myProfile.this, "Đã lưu", Toast.LENGTH_SHORT).show();
            isDataChanged = false;
            linearLayout.setVisibility(View.GONE); // Ẩn LinearLayout sau khi lưu
            GetData();
        });

        // Nút "No"
        builder.setNegativeButton("Không", (dialog, which) -> {
            dialog.dismiss();
        });

        // Hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showConfirmBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Thiết lập tiêu đề và nội dung
        builder.setTitle("Xác nhận");
        builder.setMessage("Có dư liệu chưa lưu. Bạn có muốn lưu?");

        // Nút "Yes"
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            SetData();
            Toast.makeText(myProfile.this, "Đã lưu", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(myProfile.this, TKvaBM.class);
            startActivity(intent);
        });

        // Nút "No"
        builder.setNegativeButton("Không", (dialog, which) -> {
            Intent intent = new Intent(myProfile.this, TKvaBM.class);
            startActivity(intent);
        });

        // Hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void clearFocus() {
        editUser.clearFocus();
        editEmail.clearFocus();
        editPhone.clearFocus();
        editAddress.clearFocus();

        // Ẩn bàn phím ảo nếu có
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            View currentFocus = getCurrentFocus();
            if (currentFocus != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }
}
