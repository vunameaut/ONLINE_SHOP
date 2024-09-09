package com.example.btl_android.Activity.Settings;

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

import com.example.btl_android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TKvaBM extends AppCompatActivity {

    TextView viewUser, viewEmail;
    Button btnChange;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.setting_tkvabm);

        viewUser = findViewById(R.id.tv_user);
        viewEmail = findViewById(R.id.tv_email);
        btnChange = findViewById(R.id.btnChange);
        btnBack = findViewById(R.id.ivBack);

        // Nút quay lại
        btnBack.setOnClickListener(v -> onBackPressed());

        // Nút đổi mật khẩu
        btnChange.setOnClickListener(v -> {
            Intent intent = new Intent(TKvaBM.this, ChangePass.class);
            startActivity(intent);
        });

        // Hiển thị thông tin người dùng
        showInfo();
    }

    private void showInfo() {
        // Lấy uid từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", null); // null nếu không tìm thấy

        if (uid != null) {
            // Kết nối Firebase và lấy thông tin người dùng theo uid
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dbRef = database.getReference("taikhoan").child(uid);

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Lấy dữ liệu từ Firebase
                    String user = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);

                    // Kiểm tra và hiển thị thông tin
                    if (user != null && email != null) {
                        viewUser.setHint(user);
                        viewEmail.setHint(email);
                    } else {
                        Toast.makeText(TKvaBM.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi khi không truy vấn được dữ liệu
                    Toast.makeText(TKvaBM.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Trường hợp không tìm thấy uid trong SharedPreferences
            Toast.makeText(TKvaBM.this, "Không tìm thấy UID người dùng, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
        }
    }
}
