package com.sunrin.tint;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Util.CheckString;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private Button createBtn, backBtn;
    private EditText emailET, passwordET, nicknameET;

    private String email, password, nickname;
    private boolean isValidEmail, isValidPw, isValidNickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        createBtn = findViewById(R.id.signUpBtn);
        backBtn = findViewById(R.id.backBtn);
        emailET = findViewById(R.id.emailTxt);
        passwordET = findViewById(R.id.passwordTxt);
        nicknameET = findViewById(R.id.nickNameTxt);


        createBtn.setOnClickListener(view -> {
            if (!isValidEmail)
                Toast.makeText(this, "유효한 이메일을 적어주세요", Toast.LENGTH_SHORT).show();
            else if (!isValidPw)
                Toast.makeText(this, "비밀번호는 6자리 이상이어야합니다", Toast.LENGTH_SHORT).show();
            else if (nicknameET.getText().toString().trim().length() <= 0)
                Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
            else
                SignUp();
        });
        backBtn.setOnClickListener(view -> finish());

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = s.toString().trim();
                isValidEmail = CheckString.isValidEmail(email) && email.length() > 0;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString().trim();
                isValidPw = password.length() > 0;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void SignUp() {
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        nickname = nicknameET.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        RegisterUser();
                        finish();
                    } else {
                        Toast.makeText(this, "계정 생성 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void RegisterUser() {
        User_Info user_info = new User_Info(nickname);

        firebaseFirestore
                .collection("users")
                .document(Objects.requireNonNull(mAuth.getUid()))
                .set(user_info)
                .addOnSuccessListener(command -> Toast.makeText(this, "계정 생성 성공", Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(command -> Toast.makeText(this, "warning: 닉네임이 정보되지 않았습니다.", Toast.LENGTH_SHORT).show());
    }
}
