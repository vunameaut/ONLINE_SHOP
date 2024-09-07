package com.example.btl_android.Activity.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Activity.Homepage;
import com.example.btl_android.Activity.Login;
import com.example.btl_android.Activity.Register;
import com.example.btl_android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DieuKhoan extends AppCompatActivity {

    CheckBox cbAgree;
    Button btnContinue;
    TextView tvTerms;
    ImageView btnBack;
    private static final String FILE_NAME = "dieu_khoan.txt";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("taikhoan");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.setting_dieukhoan);

        cbAgree = findViewById(R.id.cbAgree);
        btnContinue = findViewById(R.id.btnContinue);
        btnBack = findViewById(R.id.ivBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        tvTerms = findViewById(R.id.tvTerms);
        String fileContent = readFromAssets();
        tvTerms.setText(fileContent);
        tvTerms.setTextColor(getResources().getColor(R.color.black));

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", null);

        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean dieukhoan = snapshot.child(uid).child("dieukhoan").getValue(Boolean.class);

                if (dieukhoan != null && dieukhoan) {
                    btnContinue.setVisibility(View.GONE);
                    cbAgree.setChecked(true);
                    cbAgree.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnContinue.setOnClickListener(view -> {
            if (cbAgree.isChecked()) {
                Toast.makeText(DieuKhoan.this, "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.", Toast.LENGTH_LONG).show();

                String intent_uid = getIntent().getStringExtra("intent_uid");

                dbRef.child(intent_uid).child("dieukhoan").setValue(true);

                Intent intent = new Intent(this, Login.class);
                startActivity(intent);

            } else {
                Toast.makeText(DieuKhoan.this, "Vui lòng đồng ý với các điều khoản để tiếp tục", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String readFromAssets() {
        StringBuilder text = new StringBuilder();

        try {
            InputStream is = getAssets().open(DieuKhoan.FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while (true) {
                try {
                    if ((line = reader.readLine()) == null) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                text.append(line).append("\n");
            }

            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }
}