package com.sunrin.tint;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    Button signUpBtn, signInBtn;
    TextView emailText, pwText;

    String input_email, input_pw;

    // 파이어베이스 인증 초기화
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        // 유저가 로그인 되어있는지 확인하고 UI 업데이트
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInBtn = findViewById(R.id.sign_in_btn);
        signUpBtn = findViewById(R.id.sign_up_btn);
        emailText = findViewById(R.id.editTextTextEmailAddress);
        pwText = findViewById(R.id.editTextTextPassword);

        signInBtn.setOnClickListener(view -> {
            input_email = emailText.getText().toString();
            input_pw = pwText.getText().toString();
            mAuth.signInWithEmailAndPassword(input_email, input_pw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this, "로그인 : 성공", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                Toast.makeText(SignInActivity.this, "로그인 : 안 됨", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        });
        signUpBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });
    }

    //Change UI according to user data.
    public void updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"환영합니다 (" + account.getUid() + ")",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));

        }else {
            Toast.makeText(this,"로그인 되지 않았습니다",Toast.LENGTH_LONG).show();
        }
    }
}
