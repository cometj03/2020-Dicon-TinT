package com.sunrin.tint;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.sunrin.tint.Util.FirebaseLogIn;
import com.sunrin.tint.Util.UserCache;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LogInActivity extends AppCompatActivity {

    private Button signInBtn, signUpBtn;
    private EditText emailTxt, pwTxt;

    private String input_email, input_pw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        FirebaseApp.initializeApp(getApplicationContext());

        signInBtn = findViewById(R.id.sign_in_btn);
        signUpBtn = findViewById(R.id.sign_up_btn);
        emailTxt = findViewById(R.id.emailTxt);
        pwTxt = findViewById(R.id.passwordTxt);

        signInBtn.setOnClickListener(view -> LogIn());
        signUpBtn.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));

        emailTxt.addTextChangedListener(textWatcher);
        pwTxt.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            input_email = emailTxt.getText().toString().trim();
            input_pw = pwTxt.getText().toString().trim();

            // isValidEmail = CheckString.isValidEmail(input_email) && input_email.length() > 0;
            boolean isValidEmail = Patterns.EMAIL_ADDRESS.matcher(input_email).matches();
            boolean isValidPw = input_pw.length() >= 6;

            signInBtn.setEnabled(isValidEmail && isValidPw);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private void LogIn() {
        input_email = emailTxt.getText().toString().trim();
        input_pw = pwTxt.getText().toString().trim();

        Toast.makeText(this, "로그인 시도중...", Toast.LENGTH_SHORT).show();

        FirebaseLogIn
                .login(input_email, input_pw,
                        userModel -> {
                            UserCache.setUser(this, userModel);
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        },
                        errorMsg -> Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show());
    }
}
