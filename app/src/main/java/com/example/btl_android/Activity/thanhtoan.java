package com.example.btl_android.Activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class thanhtoan extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private TextView totalAmountTextView;
    private TextView paymentInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhtoan);

        // Ánh xạ các View từ layout
        qrCodeImageView = findViewById(R.id.qr_code_image_view);
        totalAmountTextView = findViewById(R.id.total_amount_text_view);
        paymentInfoTextView = findViewById(R.id.payment_info_text_view);

        // Lấy số tiền từ Intent
        long amount = getIntent().getLongExtra("TOTAL_AMOUNT", 0);

        // Hiển thị tổng tiền lên TextView
        totalAmountTextView.setText("Tổng tiền: " + amount + " VND");

        // Thông tin tài khoản, ngân hàng và số tiền
        String bankCode = "VTCB"; // Techcombank code
        String accountNumber = "6826102003";
        String note = "Thanh toan";

        // Tạo URL VIETQR với giá trị amount
        String qrData = "vietqr://" + bankCode + "/" + accountNumber + "?amount=" + amount + "&note=" + note;

        // Tạo mã QR và hiển thị
        generateQRCode(qrData);
    }

    // Hàm tạo mã QR từ chuỗi dữ liệu
    private void generateQRCode(String data) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
