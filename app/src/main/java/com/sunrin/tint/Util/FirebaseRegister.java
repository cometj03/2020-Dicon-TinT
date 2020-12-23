package com.sunrin.tint.Util;

import android.util.Patterns;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Model.UserModel;

import java.util.List;

public class FirebaseRegister {

    private static OnRegisterSuccessListener onRegisterSuccessListener;
    private static OnRegisterFailureListener onRegisterFailureListener;

    public interface OnRegisterSuccessListener {
        void onRegisterSuccess(UserModel userModel);
    }

    public interface OnRegisterFailureListener {
        void onRegisterFailed(String errorMsg);
    }

    public static void register(String email, String password, String name, OnRegisterSuccessListener s, OnRegisterFailureListener f) {
        onRegisterSuccessListener = s;
        onRegisterFailureListener = f;

        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onRegisterFailureListener.onRegisterFailed("유효한 이메일을 적어주세요.");
            return;
        }
        if (password == null || password.length() < 6) {
            onRegisterFailureListener.onRegisterFailed("비밀번호는 6자리 이상이어야 합니다.");
            return;
        }
        if (name == null || name.isEmpty()) {
            onRegisterFailureListener.onRegisterFailed("이름을 적어주세요.");
            return;
        }

        checkEmailAlreadyExists(email,
                result -> createUserData(email, name,
                        aVoid -> registerUser(email, password,
                                authResult -> onRegisterSuccessListener.onRegisterSuccess(new UserModel(name, email)))));
    }

    private static void checkEmailAlreadyExists(String email, OnSuccessListener<SignInMethodQueryResult> s) {
        FirebaseAuth
                .getInstance()
                .fetchSignInMethodsForEmail(email)
                .addOnSuccessListener(result -> {
                    List<String> signInMethods = result.getSignInMethods();
                    if (signInMethods == null || signInMethods.isEmpty())
                        s.onSuccess(result);
                    else
                        onRegisterFailureListener.onRegisterFailed("이미 존재하는 이메일입니다.");
                })
                .addOnFailureListener(e -> onRegisterFailureListener.onRegisterFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "이미 존재하는 이메일입니다.")));
    }

    private static void createUserData(String email, String name, OnSuccessListener<Void> s) {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(email)
                .set(new UserModel(name, email))
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onRegisterFailureListener.onRegisterFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "유저 데이터 업로드에 실패했습니다.")));
    }

    private static void registerUser(String email, String password, OnSuccessListener<AuthResult> s) {
        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onRegisterFailureListener.onRegisterFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "회원가입에 실패했습니다.")));
    }
}
