package com.example.btl_android.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.Activity.Settings.DieuKhoan;
import com.example.btl_android.Activity.Settings.ThongBao;
import com.example.btl_android.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    // Khai báo các phần tử
    EditText mailEditText, passEditText;
    TextInputLayout mailInputLayout, passInputLayout;
    CheckBox rememberMeCheckBox;
    Button btn_Login, btn_Register, btn_FgPass;

    FirebaseAuth auth;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("taikhoan");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login); // Liên kết layout với Activity

        auth = FirebaseAuth.getInstance(); // Khởi tạo FirebaseAuth

        Mapping(); // Gọi hàm ánh xạ

        RegisterToLogin();

        // Lưu trạng thái khi checkbox "Remember Me" được chọn hoặc bỏ chọn
        rememberMeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("remember", isChecked); // Lưu trạng thái checkbox
            editor.apply();
        });

        setupRememberMe(); // Thiết lập "Remember Me"

        // Quên mật khẩu
        btn_FgPass.setOnClickListener(view -> Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show());

        // Xử lý sự kiện khi nhấn nút Login
        btn_Login.setOnClickListener(view -> LoginToHomepage());

        // Xử lý sự kiện khi nhấn nút Register (chuyển sang màn hình đăng ký)
        btn_Register.setOnClickListener(view -> {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        });

        // Thiết lập trạng thái thông báo
        setSwitchNotif();
    }

    // Phương thức ánh xạ các phần tử
    private void Mapping() {
        mailEditText = findViewById(R.id.et_mail);
        passEditText = findViewById(R.id.et_Pass);
        mailInputLayout = findViewById(R.id.til_Email);
        passInputLayout = findViewById(R.id.til_Pass);
        rememberMeCheckBox = findViewById(R.id.cb_remember);
        btn_FgPass = findViewById(R.id.tv_FgPass);
        btn_Login = findViewById(R.id.btn_Login);
        btn_Register = findViewById(R.id.btn_Register);
    }

    // Phương thức kiểm tra trạng thái đăng nhập (nếu đã đăng nhập trước đó)
//    private void checkLogin() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        String uid = sharedPreferences.getString("uid", null); // Lấy UID từ SharedPreferences
//        if (uid != null) {
//            // Nếu người dùng đã đăng nhập, chuyển tới màn hình chính (Homepage)
//            Intent intent = new Intent(Login.this, Homepage.class);
//            intent.putExtra("uid", uid);
//            startActivity(intent);
//            finish(); // Đóng màn hình đăng nhập để người dùng không quay lại được
//        }
//    }

    // Phương thức đăng nhập
    protected void LoginToHomepage() {
        String inputEmail = mailEditText.getText().toString(); // Lấy email nhập từ EditText
        String inputPass = passEditText.getText().toString(); // Lấy mật khẩu nhập từ EditText

        // Kiểm tra tính hợp lệ của dữ liệu nhập
        if (!checkInput(inputEmail, inputPass)) {
            return; // Dừng nếu dữ liệu không hợp lệ
        }

        // Đăng nhập với FirebaseAuth
        auth.signInWithEmailAndPassword(inputEmail, inputPass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser(); // Lấy người dùng hiện tại từ Firebase
                        if (user != null) {
                            String uid = user.getUid(); // Lấy UID của người dùng

                            // Lưu UID và thông tin đăng nhập vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("uid", uid);
                            editor.putString("email", inputEmail);
                            editor.putString("password", inputPass);
                            editor.apply();

                            if (user.isEmailVerified()) {
                                // Nếu email đã được xác thực, chuyển màn hình
                                dbRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Boolean dieukhoan = snapshot.child(uid).child("dieukhoan").getValue(Boolean.class);

                                        if (dieukhoan) {
                                            Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Login.this, Homepage.class);
                                            intent.putExtra("ACTIVITY", "Login");
                                            intent.putExtra("intent_uid", uid);
                                            startActivity(intent);
                                            finish(); // Đóng màn hình đăng nhập
                                        }
                                        else {
                                            Intent intent = new Intent(Login.this, DieuKhoan.class);
                                            intent.putExtra("ACTIVITY", "Login");
                                            startActivity(intent);
                                            finish(); // Đóng màn hình đăng nhập
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else {
                                Toast.makeText(Login.this, "Vui lòng xác thực email trước khi đăng nhập.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        // Nếu đăng nhập thất bại
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        String errorMessage = "";

                        switch (errorCode) {
                            case "ERROR_INVALID_EMAIL":
                                mailInputLayout.setError("Email không đúng");
                                break;
                            case "ERROR_WRONG_PASSWORD":
                                passInputLayout.setError("Mật khẩu không đúng");
                                break;
                            case "ERROR_USER_NOT_FOUND":
                                errorMessage = "Người dùng không tồn tại.";
                                break;
                            case "ERROR_USER_DISABLED":
                                errorMessage = "Tài khoản đã bị vô hiệu hóa.";
                                break;
                            case "ERROR_TOO_MANY_REQUESTS":
                                errorMessage = "Quá nhiều yêu cầu. Vui lòng thử lại sau.";
                                break;
                            case "ERROR_NETWORK_REQUEST_FAILED":
                                errorMessage = "Lỗi mạng. Vui lòng kiểm tra kết nối của bạn.";
                                break;
                            default:
                                errorMessage = "Lỗi đăng nhập: " + errorCode;
                                break;
                        }

                        Toast.makeText(Login.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    // Phương thức kiểm tra tính hợp lệ của dữ liệu đầu vào
    protected boolean checkInput(String inputEmail, String inputPass) {
        boolean isValid = true;

        // Kiểm tra email trống
        if (inputEmail.isEmpty()) {
            mailInputLayout.setError("Vui lòng nhập email");
            mailEditText.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            mailInputLayout.setError("Email không hợp lệ");
            mailEditText.requestFocus();
            isValid = false;
        } else {
            mailInputLayout.setError(null); // Xóa lỗi nếu có
        }

        // Kiểm tra mật khẩu trống
        if (inputPass.isEmpty()) {
            passInputLayout.setError("Vui lòng nhập mật khẩu");
            passEditText.requestFocus();
            isValid = false;
        } else if (inputPass.length() < 6) { // Ví dụ, mật khẩu phải có ít nhất 6 ký tự
            passInputLayout.setError("Mật khẩu phải có ít nhất 6 ký tự");
            passEditText.requestFocus();
            isValid = false;
        } else {
            passInputLayout.setError(null); // Xóa lỗi nếu có
        }

        return isValid;
    }

    // Phương thức thiết lập chức năng "Remember Me"
    private void setupRememberMe() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isRemembered = sharedPreferences.getBoolean("remember", false);
        rememberMeCheckBox.setChecked(isRemembered); // Cập nhật trạng thái checkbox

        if (isRemembered) {
            // Nếu đã chọn "Remember Me", lấy thông tin email và mật khẩu từ SharedPreferences
            String email = sharedPreferences.getString("email", "");
            String password = sharedPreferences.getString("password", "");

            // Đặt giá trị email và mật khẩu vào EditText
            mailEditText.setText(email);
            passEditText.setText(password);
        }
    }

    private void RegisterToLogin() {
        Intent in = getIntent();
        String email = in.getStringExtra("email");
        String pass = in.getStringExtra("pass");

        if (email != null || pass != null) {
            mailEditText.setText(email);
            passEditText.setText(pass);
        }
    }

    // Phương thức thiết lập trạng thái thông báo
    private void setSwitchNotif() {
        SharedPreferences sharedPreferences = getSharedPreferences(ThongBao.PREFS_NAME, MODE_PRIVATE);
        if (!sharedPreferences.contains(ThongBao.KEY_APP_SWITCH)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(ThongBao.KEY_APP_SWITCH, true); // Bật thông báo
            editor.apply();
        }
    }
}
