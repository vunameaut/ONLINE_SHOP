package com.example.btl_android.Activity.admin.sanpham;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Adapter.admin.Admin_sanpham_adapter;
import com.example.btl_android.R;
import com.example.btl_android.item.admin.Admin_sanpham_item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class admin_sanpham extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Admin_sanpham_adapter sanphamAdapter;
    private List<Admin_sanpham_item> sanphamList;
    private DatabaseReference databaseReference;
    private FloatingActionButton btn_add;
    private EditText editTextSearch;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_sanpham);

        recyclerView = findViewById(R.id.card_view_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sanphamList = new ArrayList<>();
        sanphamAdapter = new Admin_sanpham_adapter(sanphamList, this);
        recyclerView.setAdapter(sanphamAdapter);

        editTextSearch = findViewById(R.id.editTextSearch);

        // Lấy dữ liệu từ Firebase (bảng san_pham)
        databaseReference = FirebaseDatabase.getInstance().getReference("san_pham");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanphamList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Admin_sanpham_item sanpham = new Admin_sanpham_item();

                    // Xử lý các trường sản phẩm từ Firebase
                    String tenSanPham = dataSnapshot.child("ten_san_pham").getValue(String.class);
                    String moTaSanPham = dataSnapshot.child("mo_ta").getValue(String.class);
                    String hinhAnhSanPham = dataSnapshot.child("hinh_anh").getValue(String.class);
                    int soLuongTonKho = dataSnapshot.child("so_luong_ton_kho").getValue(Integer.class);

                    // Xử lý "gia" có thể là Long hoặc String
                    Object giaObject = dataSnapshot.child("gia").getValue();
                    int giaSanPham;
                    if (giaObject instanceof Long) {
                        giaSanPham = ((Long) giaObject).intValue();
                    } else if (giaObject instanceof String) {
                        try {
                            giaSanPham = Integer.parseInt((String) giaObject);
                        } catch (NumberFormatException e) {
                            giaSanPham = 0; // Gán giá trị mặc định nếu không chuyển đổi được
                        }
                    } else {
                        giaSanPham = 0; // Giá trị mặc định nếu không xác định được kiểu
                    }

                    // Đặt giá trị vào đối tượng sản phẩm
                    sanpham.setTenSanPham(tenSanPham);
                    sanpham.setGiaSanPham(String.valueOf(giaSanPham)); // Chuyển giaSanPham thành chuỗi nếu cần
                    sanpham.setMoTaSanPham(moTaSanPham);
                    sanpham.setHinhAnhSanPham(hinhAnhSanPham);
                    sanpham.setSoLuongTonKho(soLuongTonKho);

                    // Thêm đối tượng vào danh sách
                    sanphamList.add(sanpham);
                }

                sanphamAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView

                // Tự động tìm kiếm một khoảng trắng khi dữ liệu được tải
                editTextSearch.setText(" ");
                editTextSearch.setSelection(editTextSearch.getText().length());
                sanphamAdapter.getFilter().filter(" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(admin_sanpham.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Chức năng tìm kiếm
        editTextSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sanphamAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(view -> {
            Intent addSanphamIntent = new Intent(this, add_sanpham.class);
            startActivity(addSanphamIntent);
        });
    }
}
