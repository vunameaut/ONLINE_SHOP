package com.example.btl_android.Activity.admin.donhang;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;
import com.example.btl_android.item.admin.Admin_donhang_item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class add_donhang extends AppCompatActivity {

    private EditText editTextCustomerName, editTextAddress, editTextPhone, editTextOrderDate, editTextOrderStatus, editTextTotal;
    private Button btnAddOrder;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donhang);

        // Ánh xạ các View
        editTextCustomerName = findViewById(R.id.editTextCustomerName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextOrderDate = findViewById(R.id.editTextOrderDate);
        editTextOrderStatus = findViewById(R.id.editTextOrderStatus);
        editTextTotal = findViewById(R.id.editTextTotal);
        btnAddOrder = findViewById(R.id.btnAddOrder);

        // Khởi tạo DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("don_hang");

        // Xử lý sự kiện thêm đơn hàng
        btnAddOrder.setOnClickListener(v -> checkAndAddOrder());
    }

    private void checkAndAddOrder() {
        String customerName = editTextCustomerName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String orderDate = editTextOrderDate.getText().toString().trim();
        String orderStatus = editTextOrderStatus.getText().toString().trim();
        int total = Integer.parseInt(editTextTotal.getText().toString().trim());

        // Kiểm tra các trường không được để trống
        if (customerName.isEmpty() || address.isEmpty() || phone.isEmpty() || orderDate.isEmpty() || orderStatus.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tự động tạo mã đơn hàng bằng cách sử dụng push().getKey()
        String orderCode = databaseReference.push().getKey();

        // Tạo đối tượng sản phẩm mẫu cho đơn hàng (có thể mở rộng để người dùng thêm nhiều sản phẩm)
        List<Admin_donhang_item.SanPham> sanPhamList = new ArrayList<>();
        sanPhamList.add(new Admin_donhang_item.SanPham("Sản phẩm mẫu", 1000000, 2));

        // Sử dụng constructor với danh sách sản phẩm
        Admin_donhang_item newOrder = new Admin_donhang_item(orderCode, customerName, address, phone, orderDate, orderStatus, total, sanPhamList);

        // Thêm dữ liệu vào Firebase với mã đơn hàng làm key
        databaseReference.child(orderCode).setValue(newOrder).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(add_donhang.this, "Thêm đơn hàng thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(add_donhang.this, "Thêm đơn hàng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
