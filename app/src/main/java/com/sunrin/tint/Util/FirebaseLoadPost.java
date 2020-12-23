package com.sunrin.tint.Util;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.sunrin.tint.MainScreen.Feed.FeedItem;
import com.sunrin.tint.Model.PostModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FirebaseLoadPost {

    private static OnLoadSuccessListener onLoadSuccessListener;
    private static OnLoadFailureListener onLoadFailureListener;

    public interface OnLoadSuccessListener {
        void onLoadSuccess(List<FeedItem> feedItems);
    }

    public interface OnLoadFailureListener {
        void onLoadFailed(String errorMsg);
    }

    public static void LoadPosts(Context context, OnLoadSuccessListener s, OnLoadFailureListener f) {
        onLoadSuccessListener = s;
        onLoadFailureListener = f;

        LoadPostContents(s1 -> MakePostList(context, s1));
    }

    private static void LoadPostContents(OnSuccessListener<QuerySnapshot> s) {
        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .get()
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onLoadFailureListener.onLoadFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "데이터를 불러오지 못했습니다.")));
    }

    private static void MakePostList(Context context, QuerySnapshot snapshots) {
        List<PostModel> list = new ArrayList<>();
        List<FeedItem> feedItems = new ArrayList<>();

        for (QueryDocumentSnapshot document : snapshots) {
            list.add(document.toObject(PostModel.class));
            list.sort(Comparator.reverseOrder());
        }
        for (PostModel p : list) {
            feedItems.add(p.convertIntoFeedItem(context));
        }
        onLoadSuccessListener.onLoadSuccess(feedItems);
    }

    public static void LoadImage(String imgID, OnSuccessListener<Uri> s) {
        if (imgID == null)
            return;
        FirebaseStorage
                .getInstance()
                .getReference()
                .child("images/" + imgID)
                .getDownloadUrl()
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onLoadFailureListener.onLoadFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "이미지를 불러오지 못했습니다.")));
    }
}
