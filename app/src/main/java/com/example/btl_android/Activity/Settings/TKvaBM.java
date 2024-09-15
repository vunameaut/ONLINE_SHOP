package com.example.btl_android.Activity.Settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Activity.Login;
import com.example.btl_android.Activity.Setting;
import com.example.btl_android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TKvaBM extends AppCompatActivity {

    // Khai báo các view
    TextView viewUser, viewEmail, viewPhone;
    Button btnChange, btnProfile, btnDelAcc;
    ImageView btnBack;

    // Khai báo database của Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("taikhoan");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Kích hoạt EdgeToEdge
        setContentView(R.layout.setting_tkvabm); // Set layout cho activity

        Mapping(); // Liên kết các thành phần giao diện với mã

        // Xử lý sự kiện nút quay lại
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(TKvaBM.this, Setting.class); // Chuyển về màn hình cài đặt
            startActivity(intent);
        });

        // Xử lý sự kiện nút xem hồ sơ
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(TKvaBM.this, myProfile.class); // Chuyển về màn hình hồ sơ cá nhân
            startActivity(intent);
        });

        // Xử lý sự kiện nút đổi mật khẩu
        btnChange.setOnClickListener(v -> {
            Intent intent = new Intent(TKvaBM.this, ChangePass.class); // Chuyển về màn hình đổi mật khẩu
            startActivity(intent);
        });

        // Xử lý sự kiện nút xóa tài khoản
        btnDelAcc.setOnClickListener(v -> showConfirm()); // Gọi hàm xóa tài khoản khi nhấn nút

        showInfo(); // Hiển thị thông tin người dùng
    }

    // Phương thức để liên kết các thành phần giao diện với mã (Mapping)
    private void Mapping() {
        viewUser = findViewById(R.id.tv_user);
        viewEmail = findViewById(R.id.tv_email);
        viewPhone = findViewById(R.id.tv_numPhone);
        btnProfile = findViewById(R.id.btnProfile);
        btnChange = findViewById(R.id.btnChange);
        btnDelAcc = findViewById(R.id.btnDelAccount);
        btnBack = findViewById(R.id.ivBack);
    }

    // Phương thức hiển thị thông tin người dùng từ Firebase
    private void showInfo() {
        // Khởi tạo Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("taikhoan");

        // Lấy uid từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", null);

        // Lắng nghe sự thay đổi dữ liệu từ Firebase Database
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Lấy dữ liệu của người dùng dựa trên uid
                DataSnapshot data = snapshot.child(uid);

                // Lấy giá trị của các trường username, email và số điện thoại
                String user = data.child("username").getValue(String.class);
                String email = data.child("email").getValue(String.class);
                String sdt = data.child("sdt").getValue(String.class);

                // Hiển thị dữ liệu lên giao diện
                viewUser.setHint(user);
                viewEmail.setHint(email);
                viewPhone.setHint(sdt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi trong quá trình lấy dữ liệu
                Toast.makeText(TKvaBM.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hiển thị dialog xác nhận lưu trước khi quay lại trang trước đó
    public void showConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Thiết lập tiêu đề và nội dung
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn chắc xóa tài khoản không?");

        // Nút "Yes"
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            DeleteAccount();
            Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });

        // Nút "No"
        builder.setNegativeButton("Không", (dialog, which) -> {
            Intent intent = new Intent(this, TKvaBM.class);
            startActivity(intent);
        });

        // Hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Phương thức xóa tài khoản người dùng
    private void DeleteAccount() {
        // Lấy uid từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", null);

        // Lấy instance của Firebase Authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Kiểm tra nếu người dùng hiện tại tồn tại và uid trùng khớp
        if (auth.getCurrentUser() != null && auth.getCurrentUser().getUid().equals(uid)) {
            // Xóa tài khoản của người dùng
            auth.getCurrentUser().delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Thông báo tài khoản đã được xóa thành công
                            Toast.makeText(TKvaBM.this, "Tài khoản đã được xóa thành công", Toast.LENGTH_SHORT).show();

                            // Xóa tài khoản khỏi Firebase Database
                            dbRef.child(uid).removeValue();

                            // Xóa Uid khỏi SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();  // Xóa tất cả dữ liệu
                            editor.apply();
                        } else {
                            // Xử lý lỗi nếu không thể xóa tài khoản
                            Toast.makeText(TKvaBM.this, "Không thể xóa tài khoản: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Nếu không tìm thấy người dùng hiện tại hoặc uid không trùng khớp
            Toast.makeText(TKvaBM.this, "Không tìm thấy người dùng hiện tại", Toast.LENGTH_SHORT).show();
        }
    }
}
