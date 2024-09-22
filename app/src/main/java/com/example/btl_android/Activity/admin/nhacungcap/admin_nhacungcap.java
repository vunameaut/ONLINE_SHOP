package com.example.btl_android.Activity.admin.nhacungcap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.Adapter.admin.Admin_nhacungcap_adapter;
import com.example.btl_android.R;
import com.example.btl_android.item.admin.Admin_nhacungcap_item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class admin_nhacungcap extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Admin_nhacungcap_adapter nhacungcapAdapter;
    private List<Admin_nhacungcap_item> nhacungcapList;
    private DatabaseReference databaseReference;
    private EditText editTextSearchSupplier;
    private ValueEventListener valueEventListener;

    private static final int REQUEST_CODE_UPDATE_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_nhacungcap);

        // Ánh xạ các View
        recyclerView = findViewById(R.id.supplier_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nhacungcapList = new ArrayList<>();
        nhacungcapAdapter = new Admin_nhacungcap_adapter(nhacungcapList, this);
        recyclerView.setAdapter(nhacungcapAdapter);

        editTextSearchSupplier = findViewById(R.id.editTextSearchSupplier);

        // Khởi tạo DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("nha_cung_cap");

        // Thiết lập giá trị ValueEventListener
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nhacungcapList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Admin_nhacungcap_item nhacungcap = dataSnapshot.getValue(Admin_nhacungcap_item.class);
                    if (nhacungcap != null) {
                        nhacungcapList.add(nhacungcap);
                    }
                }
                nhacungcapAdapter.notifyDataSetChanged();
                // Tìm kiếm với ký tự rỗng để hiện tất cả nhà cung cấp
                editTextSearchSupplier.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(admin_nhacungcap.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        // Tải dữ liệu nhà cung cấp
        loadNhaCungCapData();

        // Thiết lập tìm kiếm nhà cung cấp
        editTextSearchSupplier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nhacungcapAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadNhaCungCapData() {
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
        databaseReference.addValueEventListener(valueEventListener);
    }
}
