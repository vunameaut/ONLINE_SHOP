package com.example.btl_android.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.item.MuaHangAdapter;
import com.example.btl_android.R;
import com.example.btl_android.item.CartItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MuaHang extends AppCompatActivity {

    private TextView tvCustomerName, tvPhoneNumber, tvAddress, tvTotalAmount,tvOrderDate;
    private RecyclerView rvProducts;
    private MuaHangAdapter adapter;
    private List<CartItem> cartItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mua_hang);

        // Ánh xạ view
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvAddress = findViewById(R.id.tv_address);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        rvProducts = findViewById(R.id.rv_products);
        tvOrderDate = findViewById(R.id.tv_order_date);

        // Setup RecyclerView
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MuaHangAdapter(this, cartItemList);
        rvProducts.setAdapter(adapter);

        // Load thông tin người dùng và giỏ hàng
        loadOrderDate();
        loadCustomerInfo();
        loadCartItems();
    }
    private void loadOrderDate() {
        // Lấy ngày hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // Hiển thị ngày đặt hàng lên giao diện
        tvOrderDate.setText("Ngày đặt hàng: " + currentDate);
    }


    private void loadCustomerInfo() {
        String uid = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("uid", null);
        if (uid != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("taikhoan").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Lấy thông tin từ Firebase
                        String name = snapshot.child("username").getValue(String.class);
                        Long phoneLong = snapshot.child("sdt").getValue(Long.class);
                        String address = snapshot.child("diachi").getValue(String.class);

                        // Chuyển đổi Long thành String nếu cần
                        String phone = phoneLong != null ? phoneLong.toString() : "Không có số điện thoại";

                        // Hiển thị thông tin lên giao diện
                        tvCustomerName.setText(name != null ? "Tên: " + name : "Tên: N/A");
                        tvPhoneNumber.setText("Số điện thoại: " + (phone != null ? phone : "Không có số điện thoại"));
                        tvAddress.setText("Địa chỉ: " + (address != null ? address : "Không có địa chỉ"));

                    } else {
                        Log.e("MuaHang", "Không tìm thấy thông tin người dùng với UID: " + uid);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("MuaHang", "Error loading user info", error.toException());
                }
            });
        } else {
            Log.e("MuaHang", "UID không tồn tại trong SharedPreferences");
        }
    }







    private void loadCartItems() {
        String uid = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("uid", null);
        if (uid != null) {
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(uid);
            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    cartItemList.clear();
                    long totalAmount = 0;
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        CartItem item = itemSnapshot.getValue(CartItem.class);
                        if (item != null) {
                            // Ghi log thông tin sản phẩm trong giỏ hàng
                            Log.d("MuaHang", "Sản phẩm: " + item.getName() + ", Giá: " + item.getPrice() + ", Số lượng: " + item.getQuantity());

                            cartItemList.add(item);
                            totalAmount += item.getPrice() * item.getQuantity();
                        }
                    }
                    adapter.notifyDataSetChanged();

                    // Hiển thị tổng tiền kèm theo chữ "Tổng tiền: "
                    tvTotalAmount.setText("Tổng tiền: " + totalAmount + " VND");

                    // Ghi log tổng tiền
                    Log.d("MuaHang", "Tổng tiền: " + totalAmount);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("MuaHang", "Error loading cart items", error.toException());
                }
            });
        }
    }
}
