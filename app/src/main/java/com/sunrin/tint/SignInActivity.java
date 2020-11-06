package com.sunrin.tint;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sunrin.tint.Util.CheckString;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button signInBtn, signUpBtn;
    private EditText emailTxt, pwTxt;

    private String input_email, input_pw;
    private boolean isValidEmail, isValidPw;

    @Override
    protected void onStart() {
        super.onStart();
        // 유저가 로그인 되어있는지 확인 후 UI 업데이트
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        signInBtn = findViewById(R.id.sign_in_btn);
        signUpBtn = findViewById(R.id.sign_up_btn);
        emailTxt = findViewById(R.id.emailTxt);
        pwTxt = findViewById(R.id.passwordTxt);

        signInBtn.setOnClickListener(view -> SignIn());
        signUpBtn.setOnClickListener(view -> startActivity(new Intent(this, SignUpActivity.class)));

        emailTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_email = s.toString().trim();
                isValidEmail = CheckString.isValidEmail(input_email) && input_email.length() > 0;

                signInBtn.setEnabled(isValidEmail && isValidPw);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pwTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_pw = s.toString().trim();
                isValidPw = input_pw.length() >= 6;

                signInBtn.setEnabled(isValidEmail && isValidPw);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void SignIn() {
        input_email = emailTxt.getText().toString().trim();
        input_pw = pwTxt.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(input_email, input_pw)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Change UI according to user data.
    private void updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"환영합니다 (id : " + account.getUid() + ")",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else {
            Toast.makeText(this, "로그인해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}
