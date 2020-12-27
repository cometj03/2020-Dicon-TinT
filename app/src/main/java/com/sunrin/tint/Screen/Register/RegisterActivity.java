package com.sunrin.tint.Screen.Register;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sunrin.tint.R;
import com.sunrin.tint.Screen.Register.FirebaseRegister;
import com.sunrin.tint.Util.UserCache;
import com.sunrin.tint.View.LoadingDialog;

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

        LoadingDialog dialog = new LoadingDialog(this);
        dialog.setMessage("계정 생성 중...").show();

        FirebaseRegister
                .register(email, password, name,
                        userModel -> {
                            dialog.setMessage("계정 생성 성공!")
                                    .setFinishListener(() -> {
                                        UserCache.setUser(this, userModel);
                                        finish();
                                    }).finish(true);
                        },
                        errorMsg -> dialog.setMessage(errorMsg).finish(false));
    }
}
