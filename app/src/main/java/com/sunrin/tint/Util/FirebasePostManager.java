package com.sunrin.tint.Util;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sunrin.tint.Feed.FeedAdapter;
import com.sunrin.tint.Feed.FeedItem;
import com.sunrin.tint.Model.PostModel;

import java.util.ArrayList;
import java.util.Comparator;

public class FirebasePostManager {
    // TODO: Complete Class

    public interface OnUploadFailureListener {
        void onUploadFailed(String errorMsg);
    }

    // 데이터 불러온 후 adapter refresh
    public static void LoadFeedItems(FeedAdapter adapter) {
        ArrayList<FeedItem> list = new ArrayList<>();

        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(FeedItem.class));
                            list.sort(Comparator.reverseOrder());
                        }
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                });
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
}
