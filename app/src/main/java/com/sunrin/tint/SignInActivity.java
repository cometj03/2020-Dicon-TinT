package com.sunrin.tint;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    //Button signUpBtn, signInBtn;
    private FirebaseAuth mAuth;
    String email = ((EditText)findViewById(R.id.editTextTextEmailAddress)).getText().toString();
    String password = ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString();
    //String nickname = ((EditText)findViewById(R.id.editTextNickname)).getText().toString();
    String TAG = "SignInActivity";
    // 파이어베이스 인증 초기화
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



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
