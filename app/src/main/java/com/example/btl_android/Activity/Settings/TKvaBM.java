package com.example.btl_android.Activity.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TKvaBM extends AppCompatActivity {

    TextView viewUser, viewEmail;
    Button btnChange;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.setting_tkvabm);

        viewUser = findViewById(R.id.tv_user);
        viewEmail = findViewById(R.id.tv_email);
        btnChange = findViewById(R.id.btnChange);
        btnBack = findViewById(R.id.ivBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        btnChange.setOnClickListener(v -> {
            Intent intent = new Intent(TKvaBM.this, ChangePass.class);
            startActivity(intent);
        });

        showInfo();
    }

    private void showInfo() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("taikhoan");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot data = snapshot.child("vunam4280@gmail,com");

                String user = data.child("username").getValue(String.class);
                String email = data.child("email").getValue(String.class);

                viewUser.setHint(user);
                viewEmail.setHint(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TKvaBM.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}