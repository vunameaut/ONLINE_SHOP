package com.example.btl_android.Activity.Settings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.btl_android.Activity.Homepage;
import com.example.btl_android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThongBao extends AppCompatActivity {

    public static final String CHANNEL_ID = "thong_bao";
    public static final String PREFS_NAME = "ThongBaoPrefs";
    public static final String KEY_APP_SWITCH = "app_switch";
    public static final String KEY_MAIL_SWITCH = "mail_switch";

    ImageView btnBack;
    SwitchCompat swcApp, swcMail;
    SharedPreferences sharedPreferences;

    DatabaseReference dataRef;
    ValueEventListener valueEventListener;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.setting_thongbao);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        btnBack = findViewById(R.id.ivBack);
        swcApp = findViewById(R.id.swApp);
        swcMail = findViewById(R.id.swEmail);

        btnBack.setOnClickListener(v -> onBackPressed());

        saveSwitchState();

        createNotificationChannel();

        swcApp.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_APP_SWITCH, isChecked).apply();

            if (isChecked) {
                Toast.makeText(this, "Bật thông báo", Toast.LENGTH_SHORT).show();
                registerFirebaseListener();
            } else {
                Toast.makeText(this, "Tắt thông báo", Toast.LENGTH_SHORT).show();
                unregisterFirebaseListener();
            }
        });

        swcMail.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_MAIL_SWITCH, isChecked).apply();

            if (isChecked)
                Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "ThongBao";
            String description = "ThongBao";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    protected void saveSwitchState() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isAppSwitchOn = sharedPreferences.getBoolean(KEY_APP_SWITCH, false);
        boolean isMailSwitchOn = sharedPreferences.getBoolean(KEY_MAIL_SWITCH, false);

        swcApp.setChecked(isAppSwitchOn);
        swcMail.setChecked(isMailSwitchOn);
    }

    private void registerFirebaseListener() {
        dataRef = FirebaseDatabase.getInstance().getReference("san_pham");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    // Lấy thông tin sản phẩm từ Firebase Database
                    String productName = productSnapshot.child("ten_san_pham").getValue(String.class);
                    long productPrice = productSnapshot.child("gia").getValue(Long.class);

                    // Tạo và hiển thị thông báo khi có sản phẩm mới
                    sendNotification("Sản phẩm mới", "Tên: " + productName + ", Giá: " + productPrice);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi bị lỗi
            }
        };

        dataRef.addValueEventListener(valueEventListener); // Đăng ký lắng nghe
    }

    private void unregisterFirebaseListener() {
        if (dataRef != null && valueEventListener != null) {
            dataRef.removeEventListener(valueEventListener); // Hủy lắng nghe
        }
    }

    public void sendNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, Homepage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}