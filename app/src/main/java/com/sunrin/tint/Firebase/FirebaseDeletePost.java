package com.sunrin.tint.Firebase;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Models.UserModel;
import com.sunrin.tint.Util.FirebaseErrorUtil;
import com.sunrin.tint.Util.UserCache;

public class FirebaseDeletePost {

    public interface OnDeleteFailureListener {
        void onDeleteFailed(String errMsg);
    }

    public static void DeletePost(String documentID, Context context, OnSuccessListener<Void> s, OnDeleteFailureListener f) {

        UserModel user = UserCache.getUser(context);

        if (user != null && !user.getPostID().contains(documentID)) {
            f.onDeleteFailed("삭제하려는 포스트를 찾을 수 없습니다.");
            return;
        }

        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .document(documentID)
                .delete()
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> f.onDeleteFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "삭제 실패하였습니다.")));
    }
}
