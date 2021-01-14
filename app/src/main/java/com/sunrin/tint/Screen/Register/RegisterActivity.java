package com.sunrin.tint.Screen.Register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sunrin.tint.Firebase.User.FirebaseRegister;
import com.sunrin.tint.R;
import com.sunrin.tint.View.LoadingDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_email, et_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email = findViewById(R.id.emailTxt);
        et_password = findViewById(R.id.passwordTxt);

        Button createBtn = findViewById(R.id.signUpBtn);
        Button backBtn = findViewById(R.id.backBtn);
        createBtn.setOnClickListener(view -> register());
        backBtn.setOnClickListener(view -> finish());
    }

    private void register() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString();

        LoadingDialog dialog = new LoadingDialog(this);
        dialog.setMessage("계정 생성 중...").show();

        FirebaseRegister
                .register(email, password,
                        () -> {
                            dialog.setMessage("계정 생성 성공!")
                                    .setFinishListener(() -> {
                                        Intent intent = new Intent(this, Register2Activity.class);
                                        intent.putExtra("email", email);
                                        startActivity(intent);
                                        finish();
                                    }).finish(true);
                        },
                        errorMsg -> dialog.setMessage(errorMsg).finish(false));
    }
}
