package com.sunrin.tint;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button createBtn, backBtn;
    private String email, password, nickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        createBtn = findViewById(R.id.signUpBtn);
        backBtn = findViewById(R.id.backBtn);
        createBtn.setOnClickListener(view -> SignUp());
        backBtn.setOnClickListener(view -> finish());
    }

    private void SignUp() {
        email = ((EditText) findViewById(R.id.emailTxt)).getText().toString();
        password = ((EditText) findViewById(R.id.passwordTxt)).getText().toString();
        nickname = ((EditText) findViewById(R.id.nickNameTxt)).getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "계정 생성 성공", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "계정 생성 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
