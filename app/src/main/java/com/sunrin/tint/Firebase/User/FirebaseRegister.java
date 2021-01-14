package com.sunrin.tint.Firebase.User;

import android.util.Patterns;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Models.UserModel;
import com.sunrin.tint.Util.FirebaseErrorUtil;

import java.util.List;

public class FirebaseRegister {

    private static OnRegisterFailureListener onRegisterFailureListener;

    public interface OnRegisterSuccessListener {
        void onRegisterSuccess();
    }

    public interface OnRegisterFailureListener {
        void onRegisterFailed(String errorMsg);
    }

    public static void register(String email, String password, OnRegisterSuccessListener s, OnRegisterFailureListener f) {
        onRegisterFailureListener = f;

        if (email == null || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onRegisterFailureListener.onRegisterFailed("유효한 이메일을 적어주세요.");
            return;
        }

        if (password ==  null || password.length() < 6) {
            onRegisterFailureListener.onRegisterFailed("비밀번호는 6자리 이상 입력해주세요.");
            return;
        }

        checkEmailAlreadyExists(email,
                result -> uploadUserData(new UserModel(email),
                        aVoid -> registerUser(email, password,
                            authResult -> s.onRegisterSuccess())));
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

    private static void uploadUserData(UserModel userModel, OnSuccessListener<Void> s) {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(userModel.getEmail())
                .set(userModel)
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
