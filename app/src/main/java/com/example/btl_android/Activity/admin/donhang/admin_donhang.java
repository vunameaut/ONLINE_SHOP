package com.example.btl_android.Activity.admin.donhang;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Adapter.admin.Admin_donhang_adapter;
import com.example.btl_android.R;
import com.example.btl_android.item.admin.Admin_donhang_item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class admin_donhang extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Admin_donhang_adapter donhangAdapter;
    private List<Admin_donhang_item> donhangList;
    private DatabaseReference databaseReference;
    private EditText editTextSearchOrder;
    private FloatingActionButton btnAddOrder;

    private ValueEventListener valueEventListener;
    private static final int REQUEST_CODE_UPDATE_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_donhang);

        // Ánh xạ các View
        recyclerView = findViewById(R.id.order_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        donhangList = new ArrayList<>();
        donhangAdapter = new Admin_donhang_adapter(donhangList, this);
        recyclerView.setAdapter(donhangAdapter);

        editTextSearchOrder = findViewById(R.id.editTextSearchOrder);
        btnAddOrder = findViewById(R.id.btn_add_order);
        btnAddOrder.setOnClickListener(v -> {
            Intent intent = new Intent(this, add_donhang.class);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_DELETE);
        });

        // Khởi tạo DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("don_hang");

        // Lắng nghe sự kiện thay đổi dữ liệu
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donhangList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        Admin_donhang_item donHang = orderSnapshot.getValue(Admin_donhang_item.class);
                        if (donHang != null) {
                            donhangList.add(donHang);
                        }
                    }
                }
                donhangAdapter.notifyDataSetChanged();
                editTextSearchOrder.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(admin_donhang.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        // Tải dữ liệu đơn hàng
        loadDonhangData();

        // Thiết lập tìm kiếm
        editTextSearchOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                donhangAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý sự kiện click item đơn hàng để mở chi tiết
        donhangAdapter.setOnItemClickListener(donHang -> {
            Intent intent = new Intent(admin_donhang.this, AdminDonHangDetailActivity.class);
            intent.putExtra("orderItem", donHang);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_DELETE);
        });
    }

    private void loadDonhangData() {
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE_DELETE && resultCode == RESULT_OK) {
            loadDonhangData();
        }
    }
}
