package com.sunrin.tint.Firebase.User;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Models.UserModel;
import com.sunrin.tint.Util.FirebaseErrorUtil;
import com.sunrin.tint.Util.UserCache;

public class FirebaseUpdateUser {

    public static void updateUser(UserModel userModel, OnSuccessListener<Void> s, UserCache.onUpdateFailureListener f) {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(userModel.getEmail())
                .update("postID", userModel.getPostID(), "storageID", userModel.getStorageID(),
                        "lookBookID", userModel.getLookBookID(), "userFilters", userModel.getUserFilters())
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> f.onUpdateFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "사용자 업데이트에 실패했습니다.")));
    }
}
