package com.example.btl_android.Activity.admin.sanpham;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android.Activity.admin.taikhoan.add_taikhoan;
import com.example.btl_android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class admin_sanpham extends AppCompatActivity {

    private FloatingActionButton btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_sanpham);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(view -> {
            Intent intent = new Intent(this, add_sanpham.class);
            startActivity(intent);
        });
    }
}