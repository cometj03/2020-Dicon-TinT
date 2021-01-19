package com.sunrin.tint.Firebase.DownLoad;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Models.LookBookModel;
import com.sunrin.tint.Util.FirebaseErrorUtil;

public class FirebaseLoadOneLB {

    public interface OnLoadLBSuccessListener {
        void onLoadLBSuccess(LookBookModel lookBook);
    }

    public static void LoadLookBook(String lookBookID, OnLoadLBSuccessListener s, FirebaseLoadAllPost.OnLoadFailureListener f) {
        if (lookBookID.length() <= 0) {
            f.onLoadFailed("해당 룩북 고유 아이디가 존재하지 않습니다.");
            return;
        }

        FirebaseFirestore
                .getInstance()
                .collection("lookbooks")
                .document(lookBookID)
                .get()
                .addOnSuccessListener(document -> s.onLoadLBSuccess(document.toObject(LookBookModel.class)))
                .addOnFailureListener(e -> f.onLoadFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "룩북 데이터를 불러오지 못했습니다.")));
    }
}
