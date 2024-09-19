package com.example.btl_android.Activity.admin.sanpham;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.btl_android.R;
import com.example.btl_android.item.admin.Admin_sanpham_item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdminSanphamDetailActivity extends AppCompatActivity {

    private EditText productNameEditText, productPriceEditText, productDescriptionEditText, productStockEditText, productCategoryEditText;
    private ImageView productImageView;
    private Button btnUpdateProduct, btnDeleteProduct;
    private DatabaseReference databaseReference;
    private String productId;

    private static final String TAG = "AdminSanphamDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_product_detail);

        // Liên kết các view
        productNameEditText = findViewById(R.id.productNameEditText);
        productPriceEditText = findViewById(R.id.productPriceEditText);
        productDescriptionEditText = findViewById(R.id.productDescriptionEditText);
        productStockEditText = findViewById(R.id.productStockEditText);
        productCategoryEditText = findViewById(R.id.productCategoryEditText);
        productImageView = findViewById(R.id.productImageView);

        btnUpdateProduct = findViewById(R.id.btn_update_product);
        btnDeleteProduct = findViewById(R.id.btn_delete_product);

        // Thiết lập Toolbar và nút back
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Nhận đối tượng Admin_sanpham_item từ Intent
        Admin_sanpham_item sanPham = (Admin_sanpham_item) getIntent().getSerializableExtra("productItem");

        if (sanPham == null) {
            Toast.makeText(this, "Sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị dữ liệu trong các EditText và ImageView
        productNameEditText.setText(sanPham.getTenSanPham());
        productPriceEditText.setText(String.valueOf(sanPham.getGia()));
        productDescriptionEditText.setText(sanPham.getMoTa());
        productStockEditText.setText(String.valueOf(sanPham.getSoLuongTonKho()));
        productCategoryEditText.setText(sanPham.getLoai());

        // Log thông tin sản phẩm
        Log.d(TAG, "Sản phẩm thông tin:");
        Log.d(TAG, "Tên sản phẩm: " + sanPham.getTenSanPham());
        Log.d(TAG, "Giá: " + sanPham.getGia());
        Log.d(TAG, "Mô tả: " + sanPham.getMoTa());
        Log.d(TAG, "Số lượng tồn kho: " + sanPham.getSoLuongTonKho());
        Log.d(TAG, "Loại: " + sanPham.getLoai());
        Log.d(TAG, "UID: " + sanPham.getUid());
        Log.d(TAG, "URL ảnh: " + sanPham.getHinhAnh());

        // Lưu productId để sử dụng cho các thao tác cập nhật và xóa
        productId = sanPham.getUid();  // Sử dụng UID của sản phẩm từ Firebase

        // Load ảnh sản phẩm bằng Glide
        String productImageUrl = sanPham.getHinhAnh();
        if (productImageUrl != null && !productImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(productImageUrl)
                    .placeholder(R.drawable.ic_imagnot)
                    .error(R.drawable.ic_error)
                    .into(productImageView);
        }

        // Khởi tạo DatabaseReference với productId
        databaseReference = FirebaseDatabase.getInstance().getReference("san_pham").child(productId);

        // Xử lý sự kiện nhấn nút Cập nhật
        btnUpdateProduct.setOnClickListener(v -> updateProduct());

        // Xử lý sự kiện nhấn nút Xóa
        btnDeleteProduct.setOnClickListener(v -> deleteProduct());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // Xử lý nút back trên Toolbar
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateProduct() {
        String updatedName = productNameEditText.getText().toString().trim();
        int updatedPrice;
        String updatedDescription = productDescriptionEditText.getText().toString().trim();
        String updatedCategory = productCategoryEditText.getText().toString().trim();
        int updatedStock;

        try {
            updatedPrice = Integer.parseInt(productPriceEditText.getText().toString().trim());
            updatedStock = Integer.parseInt(productStockEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá và số lượng tồn kho không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo Map để lưu các thay đổi
        Map<String, Object> updates = new HashMap<>();
        updates.put("ten_san_pham", updatedName);
        updates.put("gia", updatedPrice);
        updates.put("mo_ta", updatedDescription);
        updates.put("so_luong_ton_kho", updatedStock);
        updates.put("loai", updatedCategory);

        // Cập nhật dữ liệu vào Firebase
        databaseReference.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminSanphamDetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);  // Gửi kết quả thành công
                finish();  // Kết thúc Activity và quay lại Activity trước
            } else {
                Toast.makeText(AdminSanphamDetailActivity.this, "Cập nhật thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProduct() {
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminSanphamDetailActivity.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);  // Gửi kết quả thành công
                finish();  // Kết thúc Activity và quay lại Activity trước
            } else {
                Toast.makeText(AdminSanphamDetailActivity.this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
