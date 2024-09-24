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

public class AdminDonHangDetailActivity extends AppCompatActivity {

    private EditText editTextOrderCode, editTextCustomerName, editTextAddress, editTextPhone, editTextOrderDate, editTextOrderStatus, editTextTotal;
    private Button btnUpdateOrder, btnDeleteOrder;
    private DatabaseReference databaseReference;
    private String orderCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_don_hang_detail);

        // Ánh xạ các View
        editTextOrderCode = findViewById(R.id.editTextOrderCode);
        editTextCustomerName = findViewById(R.id.editTextCustomerName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextOrderDate = findViewById(R.id.editTextOrderDate);
        editTextOrderStatus = findViewById(R.id.editTextOrderStatus);
        editTextTotal = findViewById(R.id.editTextTotal);
        btnUpdateOrder = findViewById(R.id.btn_update_order);
        btnDeleteOrder = findViewById(R.id.btn_delete_order);

        // Nhận đối tượng AdminDonhangItem từ Intent
        Admin_donhang_item order = (Admin_donhang_item) getIntent().getSerializableExtra("orderItem");

        if (order == null) {
            Toast.makeText(this, "Đơn hàng không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị dữ liệu trong các EditText
        editTextOrderCode.setText(order.getMaDonHang());
        editTextCustomerName.setText(order.getTenKhachHang());
        editTextAddress.setText(order.getDiaChi());
        editTextPhone.setText(order.getSoDienThoai());
        editTextOrderDate.setText(order.getNgayDatHang());
        editTextOrderStatus.setText(order.getTrangThai());
        editTextTotal.setText(String.valueOf(order.getTongTien()));

        // Lưu orderCode để sử dụng cho các thao tác cập nhật và xóa
        orderCode = order.getMaDonHang();
        databaseReference = FirebaseDatabase.getInstance().getReference("don_hang").child(orderCode);

        // Xử lý sự kiện nhấn nút Cập nhật
        btnUpdateOrder.setOnClickListener(v -> updateOrder());

        // Xử lý sự kiện nhấn nút Xóa
        btnDeleteOrder.setOnClickListener(v -> deleteOrder());
    }

    private void updateOrder() {
        String updatedCustomerName = editTextCustomerName.getText().toString().trim();
        String updatedAddress = editTextAddress.getText().toString().trim();
        String updatedPhone = editTextPhone.getText().toString().trim();
        String updatedOrderDate = editTextOrderDate.getText().toString().trim();
        String updatedOrderStatus = editTextOrderStatus.getText().toString().trim();
        int updatedTotal = Integer.parseInt(editTextTotal.getText().toString().trim());

        // Kiểm tra các trường không được để trống
        if (updatedCustomerName.isEmpty() || updatedAddress.isEmpty() || updatedPhone.isEmpty() || updatedOrderDate.isEmpty() || updatedOrderStatus.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin lên Firebase dựa trên orderCode
        Admin_donhang_item updatedOrder = new Admin_donhang_item(orderCode, updatedCustomerName, updatedAddress, updatedPhone, updatedOrderDate, updatedOrderStatus, updatedTotal, null);
        databaseReference.setValue(updatedOrder).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminDonHangDetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AdminDonHangDetailActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteOrder() {
        // Xóa đơn hàng từ Firebase dựa trên orderCode
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminDonHangDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AdminDonHangDetailActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
