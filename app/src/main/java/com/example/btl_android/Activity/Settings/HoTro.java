package com.example.btl_android.Activity.Settings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;

public class HoTro extends AppCompatActivity {

    ImageView btnBack;
    EditText InputEmail, InputDescription;
    Button btnChooseImage, btnSendReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.setting_hotro);

        Mapping();

        sendMail();
    }

    protected void Mapping() {
        btnBack = findViewById(R.id.ivBack);
        InputEmail = findViewById(R.id.et_email);
        InputDescription = findViewById(R.id.et_description);
        btnChooseImage = findViewById(R.id.btn_choose_image);
        btnSendReport = findViewById(R.id.btn_send_report);
    }

    protected void sendMail() {
        String email = "20212504@eaut.edu.vn";

    }
}