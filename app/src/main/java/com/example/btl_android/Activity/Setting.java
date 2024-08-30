package com.example.btl_android.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Activity.Settings.CongDong;
import com.example.btl_android.Activity.Settings.DieuKhoan;
import com.example.btl_android.Activity.Settings.GioiThieu;
import com.example.btl_android.R;

public class Setting extends AppCompatActivity {

    ImageView btnBack;
    Button btnCaiDatChat, btnCaiDatThongBao, btnCaiDatAnToanBaoMat, btnCaiDatNgonNgu,
            btnTrungTamHoTro, btnTieuChuanCongDong, btnDieuKhoan, btnGioiThieu, btnDangXuat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnBack =  findViewById(R.id.ivBack);
        btnCaiDatChat = findViewById(R.id.btnCaiDatChat);
        btnCaiDatThongBao = findViewById(R.id.btnCaiDatThongBao);
        btnCaiDatAnToanBaoMat = findViewById(R.id.btnCaiDatAnToanBaoMat);
        btnCaiDatNgonNgu = findViewById(R.id.btnCaiDatNgonNgu);
        btnTrungTamHoTro = findViewById(R.id.btnTrungTamHoTro);
        btnTieuChuanCongDong = findViewById(R.id.btnTieuChuanCongDong);
        btnDieuKhoan = findViewById(R.id.btnDieuKhoan);
        btnGioiThieu = findViewById(R.id.btnGioiThieu);
        btnDangXuat = findViewById(R.id.btnDangXuat);

        // Đăng ký sự kiện nhấn
        btnCaiDatChat.setOnClickListener(view -> showToast("Cài đặt Chat"));
        btnCaiDatThongBao.setOnClickListener(view -> showToast("Cài đặt Thông báo"));
        btnCaiDatAnToanBaoMat.setOnClickListener(view -> showToast("Cài đặt An toàn và bảo mật"));
        btnCaiDatNgonNgu.setOnClickListener(view -> showToast("Cài đặt Ngôn ngữ"));

        btnTrungTamHoTro.setOnClickListener(view -> showToast("Trung tâm hỗ trợ"));
        btnTieuChuanCongDong.setOnClickListener(view -> {
            Intent intent = new Intent(this, CongDong.class);
            startActivity(intent);
        });
        btnDieuKhoan.setOnClickListener(view -> {
            Intent intent = new Intent(this, DieuKhoan.class);
            startActivity(intent);
        });
        btnGioiThieu.setOnClickListener(view -> {
            Intent intent = new Intent(this, GioiThieu.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> onBackPressed());
        btnDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}