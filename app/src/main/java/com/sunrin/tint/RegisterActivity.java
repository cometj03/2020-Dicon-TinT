package com.sunrin.tint;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sunrin.tint.Util.FirebaseRegister;
import com.sunrin.tint.Util.UserCache;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailET, passwordET, nicknameET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button createBtn = findViewById(R.id.signUpBtn);
        Button backBtn = findViewById(R.id.backBtn);
        emailET = findViewById(R.id.emailTxt);
        passwordET = findViewById(R.id.passwordTxt);
        nicknameET = findViewById(R.id.nickNameTxt);

        createBtn.setOnClickListener(view -> register());
        backBtn.setOnClickListener(view -> finish());
    }

    private void register() {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String name = nicknameET.getText().toString();

        Toast.makeText(this, "계정 생성 시도중...", Toast.LENGTH_SHORT).show();

        FirebaseRegister
                .register(email, password, name,
                        userModel -> {
                            Toast.makeText(this, "계정 생성 성공", Toast.LENGTH_SHORT).show();
                            UserCache.setUser(this, userModel);
                            finish();
                        },
                        errorMsg -> Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show());
    }
}
