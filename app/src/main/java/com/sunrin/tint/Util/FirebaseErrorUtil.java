package com.sunrin.tint.Util;

import com.google.firebase.FirebaseNetworkException;

public class FirebaseErrorUtil {
    public static String getErrorMessage(Exception e, String msg) {
        return e instanceof FirebaseNetworkException ? "인터넷 연결을 확인해주세요." : msg;
    }
}
