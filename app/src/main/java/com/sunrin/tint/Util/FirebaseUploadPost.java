package com.sunrin.tint.Util;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Model.PostModel;

import java.util.List;

public class FirebaseUploadPost {

    public interface OnUploadFailureListener {
        void onUploadFailed(String errorMsg);
    }

    public static void UploadPost(Context mContext, PostModel postModel, OnSuccessListener<Void> s, OnUploadFailureListener f) {
        // TODO: Use UserCache

        String username = SharedPreferenceUtil.getPrefUsername(mContext);
        String userEmail = SharedPreferenceUtil.getPrefUserEmail(mContext);
        postModel.setUserName(username);
        postModel.setUserEmail(userEmail);

        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .document()
                .set(postModel)
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> f.onUploadFailed(FirebaseErrorUtil.getErrorMessage(e, "업로드에 실패하였습니다.")));
    }

    public static void UploadImages(List<Uri> images) {

    }
}
