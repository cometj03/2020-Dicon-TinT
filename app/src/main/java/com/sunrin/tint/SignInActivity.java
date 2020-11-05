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

public class SignInActivity extends AppCompatActivity {

    //Button signUpBtn, signInBtn;
    private FirebaseAuth mAuth;
    String email = ((EditText)findViewById(R.id.editTextTextEmailAddress)).getText().toString();
    String password = ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString();
    //String nickname = ((EditText)findViewById(R.id.editTextNickname)).getText().toString();
    String TAG = "SignInActivity";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.sign_in_btn).setOnClickListener(onClickListener);
        findViewById(R.id.sign_up_btn).setOnClickListener(onClickListener);
        //signUpBtn = findViewById(R.id.sign_up_btn);

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.sign_in_btn:
                    SignIn();
                case R.id.sign_up_btn:
                    gotosignup();
            }
        }
    };



    private void SignIn(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }

                });
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void gotosignup(){
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            Toast.makeText(this,"로그인 성공",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,MainActivity.class));

        }else {
            Toast.makeText(this,"로그인 실패",Toast.LENGTH_LONG).show();
        }
    }
}
