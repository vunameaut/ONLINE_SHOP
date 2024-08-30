package com.example.btl_android.Activity.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;

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
    private static final String PREFS_NAME = "DieuKhoanPrefs";
    private static final String KEY_AGREED = "hasAgreed";

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

        // Đọc trạng thái từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean hasAgreed = prefs.getBoolean(KEY_AGREED, false);

        if (hasAgreed) {
//            cbAgree.setChecked(true);
//            cbAgree.setEnabled(false); // Không cho phép người dùng thay đổi trạng thái của CheckBox
//            btnContinue.setVisibility(View.GONE); // Ẩn nút "Tiếp tục"

            cbAgree.setChecked(false);
            cbAgree.setEnabled(true);
            btnContinue.setVisibility(View.VISIBLE);
        }

        btnContinue.setOnClickListener(view -> {
            if (cbAgree.isChecked()) {
                Toast.makeText(DieuKhoan.this, "Bạn đã đồng ý với các điều khoản", Toast.LENGTH_SHORT).show();

                // Lưu trạng thái đồng ý vào SharedPreferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(KEY_AGREED, true);
                editor.apply();

                btnContinue.setVisibility(View.GONE);
                cbAgree.setEnabled(false);
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