package com.sunrin.tint;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Model.UserModel;
import com.sunrin.tint.Util.CheckString;
import com.sunrin.tint.Util.SharedPreferenceUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private Button createBtn, backBtn;
    private EditText emailET, passwordET, nicknameET;

    private String email, password, name;
    private boolean isValidEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createBtn = findViewById(R.id.signUpBtn);
        backBtn = findViewById(R.id.backBtn);
        emailET = findViewById(R.id.emailTxt);
        passwordET = findViewById(R.id.passwordTxt);
        nicknameET = findViewById(R.id.nickNameTxt);


        createBtn.setOnClickListener(view -> {
            if (!isValidEmail)
                Toast.makeText(this, "유효한 이메일을 적어주세요", Toast.LENGTH_SHORT).show();
            else if (passwordET.getText().toString().trim().length() < 6)
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
                isValidEmail = email.length() > 0 && CheckString.isValidEmail(email);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void SignUp() {
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        name = nicknameET.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        RegisterUser();
                    } else {
                        Toast.makeText(this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void RegisterUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserModel userInfo = new UserModel(name, email);

        firebaseFirestore
                .collection("users")
                .document(firebaseUser.getEmail())
                .set(userInfo)
                .addOnSuccessListener(command -> {
                    SharedPreferenceUtil.setPrefUsername(RegisterActivity.this, name);
                    finish();
                })
                .addOnFailureListener(command -> Toast.makeText(this, "정보가 저장되지 않았습니다.", Toast.LENGTH_SHORT).show());
    }
}
