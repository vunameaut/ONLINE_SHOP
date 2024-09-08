package com.example.btl_android.Activity.Settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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

    TextView viewUser, viewEmail, viewPhone;
    Button btnChange, btnProfile;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.setting_tkvabm);

        viewUser = findViewById(R.id.tv_user);
        viewEmail = findViewById(R.id.tv_email);
        viewPhone = findViewById(R.id.tv_numPhone);
        btnProfile = findViewById(R.id.btnProfile);
        btnChange = findViewById(R.id.btnChange);
        btnBack = findViewById(R.id.ivBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(TKvaBM.this, myProfile.class);
            startActivity(intent);
        });

        btnChange.setOnClickListener(v -> {
            Intent intent = new Intent(TKvaBM.this, ChangePass.class);
            startActivity(intent);
        });

        showInfo();
    }

    private void showInfo() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("taikhoan");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", null);

        dbRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot data = snapshot.child(uid);

                String user = data.child("username").getValue(String.class);
                String email = data.child("email").getValue(String.class);
                String sdt = data.child("sdt").getValue(String.class);

                viewUser.setHint(user);
                viewEmail.setHint(email);
                viewPhone.setHint(sdt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TKvaBM.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}