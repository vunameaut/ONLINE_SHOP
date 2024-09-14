package com.example.btl_android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.btl_android.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetailActivity extends AppCompatActivity {

    // Các thành phần giao diện
    private ImageView productImageView;
    private TextView productNameTextView, productPriceTextView, productDescriptionTextView, productStockTextView, productCategoryTextView;
    private Button buttonBuy, buttonAddToCart;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

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

        // Khởi tạo FirebaseAuth và DatabaseReference
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Khởi tạo các thành phần giao diện
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

        // Hiển thị thông tin chi tiết sản phẩm
        productNameTextView.setText(productName);
        productPriceTextView.setText(String.format("%,d VND", productPrice));
        productDescriptionTextView.setText(productDescription);
        productStockTextView.setText("Còn lại: " + productStock);
        productCategoryTextView.setText("Loại: " + productCategory);

        // Sử dụng Glide để tải và hiển thị ảnh sản phẩm
        Glide.with(this).load(productImage).into(productImageView);

        // Xử lý sự kiện khi nhấn nút "Mua"
        buttonBuy.setOnClickListener(v -> {
            // Logic mua hàng
        });

        // Xử lý sự kiện khi nhấn nút "Thêm vào giỏ hàng"
        buttonAddToCart.setOnClickListener(v -> addToCart());
    }

    // Hàm thêm sản phẩm vào giỏ hàng
    private void addToCart() {
        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("ten_san_pham");
        long productPrice = intent.getLongExtra("gia", 0);
        String productImage = intent.getStringExtra("hinh_anh");

        // Lấy ID người dùng hiện tại
        String userId = firebaseAuth.getCurrentUser().getUid();

        // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
        databaseReference.child("cart").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean productExists = false;
                int itemCount = 0;

                // Kiểm tra từng sản phẩm trong giỏ hàng
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    itemCount++;
                    CartItem existingCartItem = snapshot.getValue(CartItem.class);

                    if (existingCartItem != null && existingCartItem.getName().equals(productName)) {
                        // Nếu sản phẩm đã tồn tại, tăng số lượng
                        int currentQuantity = existingCartItem.getQuantity();
                        snapshot.getRef().child("quantity").setValue(currentQuantity + 1);
                        Toast.makeText(ProductDetailActivity.this, "Đã tăng số lượng sản phẩm!", Toast.LENGTH_SHORT).show();
                        productExists = true;
                        break;
                    }
                }

                if (!productExists) {
                    // Nếu sản phẩm chưa có trong giỏ, thêm sản phẩm mới
                    String cartItemId = "item " + (itemCount + 1); // Tạo ID cho sản phẩm
                    CartItem newCartItem = new CartItem(productName, productPrice, 1, productImage);
                    databaseReference.child("cart").child(userId).child(cartItemId).setValue(newCartItem)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thất bại.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                Toast.makeText(ProductDetailActivity.this, "Không thể truy cập giỏ hàng.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Class đại diện cho một sản phẩm trong giỏ hàng
    public static class CartItem {
        private String name;
        private long price;
        private int quantity;
        private String imageUrl;

        // Constructor mặc định (cần thiết cho Firebase)
        public CartItem() {
        }

        // Constructor có tham số
        public CartItem(String name, long price, int quantity, String imageUrl) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.imageUrl = imageUrl;
        }

        // Getter và Setter cho các thuộc tính
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getPrice() {
            return price;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    // Xử lý khi người dùng nhấn nút "Back" trên Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();  // Quay lại màn hình trước đó
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
