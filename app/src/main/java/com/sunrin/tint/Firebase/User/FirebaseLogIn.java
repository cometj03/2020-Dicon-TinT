package com.sunrin.tint.Firebase.User;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Models.UserModel;
import com.sunrin.tint.Util.FirebaseErrorUtil;

import java.util.List;

public class FirebaseLogIn {

    private static OnLoginFailureListener onLoginFailureListener;

    public interface OnLoginSuccessListener {
        void onLoginSuccess(UserModel userModel);
    }

    public interface OnLoginFailureListener {
        void onLoginFailed(String errorMsg);
    }

    public static void login(String email, String password, OnLoginSuccessListener s, OnLoginFailureListener f) {
        onLoginFailureListener = f;

        checkEmailValid(email,
                result -> getUserData(email,
                        document -> signIn(email, password,
                                authResult -> s.onLoginSuccess(document.toObject(UserModel.class)))));
    }

    private static void checkEmailValid(String email, OnSuccessListener<SignInMethodQueryResult> s) {
        FirebaseAuth
                .getInstance()
                .fetchSignInMethodsForEmail(email)
                .addOnSuccessListener(result -> {
                    List<String> signInMethods = result.getSignInMethods();
                    if (signInMethods != null && !signInMethods.isEmpty())
                        s.onSuccess(result);
                    else
                        onLoginFailureListener.onLoginFailed("존재하지 않는 이메일입니다.");
                })
                .addOnFailureListener(e -> onLoginFailureListener.onLoginFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "존재하지 않는 이메일입니다.")));
    }

    private static void getUserData(String email, OnSuccessListener<DocumentSnapshot> s) {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(email)
                .get()
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onLoginFailureListener.onLoginFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "유저 데이터가 존재하지 않습니다.")));
    }

    private static void signIn(String email, String password, OnSuccessListener<AuthResult> s) {
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onLoginFailureListener.onLoginFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "비밀번호가 올바르지 않습니다.")));
    }
}
