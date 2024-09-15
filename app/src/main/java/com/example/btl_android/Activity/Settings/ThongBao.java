package com.example.btl_android.Activity.Settings;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.btl_android.R;

public class ThongBao extends AppCompatActivity {

    public static final String PREFS_NAME = "ThongBaoPrefs";
    public static final String KEY_APP_SWITCH = "app_switch";
    public static final String KEY_MAIL_SWITCH = "mail_switch";

    private ImageView btnBack;
    private SwitchCompat swcApp, swcMail;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_thongbao);

        Mapping();
        requestNotificationPermission();
        loadSwitchState();

        btnBack.setOnClickListener(v -> onBackPressed());

        swcApp.setOnCheckedChangeListener((buttonView, isChecked) -> handleAppSwitch(isChecked));
        swcMail.setOnCheckedChangeListener((buttonView, isChecked) -> handleMailSwitch(isChecked));
    }

    private void Mapping() {
        btnBack = findViewById(R.id.ivBack);
        swcApp = findViewById(R.id.swApp);
        swcMail = findViewById(R.id.swEmail);
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    private void handleAppSwitch(boolean isChecked) {
        sharedPreferences.edit().putBoolean(KEY_APP_SWITCH, isChecked).apply();
        if (isChecked) {
            Toast.makeText(this, "Bật thông báo", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tắt thông báo", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleMailSwitch(boolean isChecked) {
        sharedPreferences.edit().putBoolean(KEY_MAIL_SWITCH, isChecked).apply();
        if (isChecked) {
            Toast.makeText(this, "Bật thông báo về mail", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tắt thông báo về mail", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSwitchState() {
        boolean isAppSwitchOn = sharedPreferences.getBoolean(KEY_APP_SWITCH, false);
        boolean isMailSwitchOn = sharedPreferences.getBoolean(KEY_MAIL_SWITCH, false);

        swcApp.setChecked(isAppSwitchOn);
        swcMail.setChecked(isMailSwitchOn);
    }
}
