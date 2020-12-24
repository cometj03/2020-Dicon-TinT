package com.sunrin.tint.Util;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Model.UserModel;

public class FirebaseUpdateUser {
    public static void updateIDs(UserModel userModel, Context context) {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(userModel.getEmail())
                .update("postID", userModel.getPostID(), "storageID", userModel.getStorageID(), "userFilters", userModel.getUserFilters())
                .addOnFailureListener(e -> Toast.makeText(context,
                        FirebaseErrorUtil.getErrorMessage(e, "사용자 업데이트가 실패했습니다."), Toast.LENGTH_SHORT).show());
    }
}
