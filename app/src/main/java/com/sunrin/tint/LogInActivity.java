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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Util.CheckString;
import com.sunrin.tint.Util.SharedPreferenceUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button signInBtn, signUpBtn;
    private EditText emailTxt, pwTxt;

    private String input_email, input_pw;
    private boolean isValidEmail, isValidPw;

    @Override
    protected void onStart() {
        super.onStart();
        // 유저가 로그인 되어있는지 확인 후 UI 업데이트
        // 스플래시 화면에 작성
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

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

            isValidEmail = CheckString.isValidEmail(input_email) && input_email.length() > 0;
            isValidPw = input_pw.length() >= 6;

            signInBtn.setEnabled(isValidEmail && isValidPw);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private void LogIn() {
        input_email = emailTxt.getText().toString().trim();
        input_pw = pwTxt.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(input_email, input_pw)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null)
                            getUserName(user);
                        else
                            Toast.makeText(this, "user is not exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 유저 이름 firestore에서 불러오기
    private void getUserName(FirebaseUser user) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore
                .collection("users")
                .document(user.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String userName = (String) document.getData().get("userName");
                            SharedPreferenceUtil.setPrefUsername(LogInActivity.this, userName);
                            SharedPreferenceUtil.setPrefUserEmail(LogInActivity.this, user.getEmail());

                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    //Change UI according to user data.
    private void updateUI(FirebaseUser user){
        if(user != null) {
            //Toast.makeText(this,"환영합니다 (id : " + account.getUid() + ")",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else {
            Toast.makeText(this, "로그인해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}
