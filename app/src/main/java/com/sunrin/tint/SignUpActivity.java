package com.sunrin.tint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SingUpActivity";

    private String email, password, nickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_sign_up).setOnClickListener(onClickListener);
        findViewById(R.id.buttonBack).setOnClickListener(onClickListener);
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button_sign_up:
                    SignUp();
                    break;
                case R.id.buttonBack:
                    finish();
                    break;
            }
        }
    };



    private void SignUp(){
        email = ((EditText)findViewById(R.id.editTextTextEmailAddress)).getText().toString();
        password = ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString();
        nickname = ((EditText)findViewById(R.id.editTextNickname)).getText().toString();

       //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       //FirebaseFirestore db = FirebaseFirestore.getInstance();

       //User_Info user_info = new User_Info(email,password,nickname);
        //assert user != null;
        //db.collection("users").document(user.getUid()).set(user_info);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "인증 : 안 됨", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    //Change UI according to user data.
    public void updateUI(FirebaseUser account) {
        if (account != null){
            Toast.makeText(this,"환영합니다 (" + account.getUid() + ")",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else {
            Toast.makeText(this,"로그인 되지 않았습니다",Toast.LENGTH_LONG).show();
        }
    }
}
