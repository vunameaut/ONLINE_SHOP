package com.example.btl_android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.btl_android.R;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView, productPriceTextView, productDescriptionTextView, productStockTextView, productCategoryTextView;
    private Button buttonBuy, buttonAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Khởi tạo Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Kích hoạt nút "Back" trong Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Các thành phần còn lại của chi tiết sản phẩm
        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        productStockTextView = findViewById(R.id.productStockTextView);
        productCategoryTextView = findViewById(R.id.productCategoryTextView);
        buttonBuy = findViewById(R.id.buttonBuy);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("ten_san_pham");
        long productPrice = intent.getLongExtra("gia", 0);
        String productImage = intent.getStringExtra("hinh_anh");
        String productDescription = intent.getStringExtra("mo_ta");
        int productStock = intent.getIntExtra("so_luong_ton_kho", 0);
        String productCategory = intent.getStringExtra("loai_san_pham");

        // Hiển thị thông tin sản phẩm
        productNameTextView.setText(productName);
        productPriceTextView.setText(String.format("%,d VND", productPrice));
        productDescriptionTextView.setText(productDescription);
        productStockTextView.setText("Còn lại: " + productStock);
        productCategoryTextView.setText("Loại: " + productCategory);
        Glide.with(this).load(productImage).into(productImageView);

        // Xử lý khi nhấn nút "Mua"
        buttonBuy.setOnClickListener(v -> {
            // Thêm logic mua hàng
        });

        // Xử lý khi nhấn nút "Thêm vào giỏ hàng"
        buttonAddToCart.setOnClickListener(v -> {
            // Thêm logic thêm vào giỏ hàng
        });


    }
    // Xử lý khi người dùng nhấn vào nút "Back"
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();  // Quay lại Activity trước đó
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
