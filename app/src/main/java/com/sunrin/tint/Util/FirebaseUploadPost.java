package com.sunrin.tint.Util;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUploadPost {
    private static List<String> urlList;

    private static OnUploadSuccessListener onUploadSuccessListener;
    private static OnUploadFailureListener onUploadFailureListener;

    public interface OnUploadSuccessListener {
        void onUploadSuccess(String documentID);
    }

    public interface OnUploadFailureListener {
        void onUploadFailed(String errorMsg);
    }

    public static void Upload(Context context, PostModel postModel, OnUploadSuccessListener s, OnUploadFailureListener f) {
        onUploadSuccessListener = s;
        onUploadFailureListener = f;

        urlList = new ArrayList<>();

        String filename = DateUtil.getFileNameWithDate();
        postModel.setId("Post_" + filename);

        UserModel userModel = UserCache.getUser(context);
        postModel.setUserName(userModel.getName());
        postModel.setUserEmail(userModel.getEmail());

        // 이미지 확장자 검사
        if (!postModel.getImages().isEmpty() && !checkImagesExtension(postModel.getImages())) {
            onUploadFailureListener.onUploadFailed("올바른 형식의 이미지를 업로드 해주세요 (jpeg/png)");
            return;
        }

        uploadImages(postModel.getImages(), filename,
                uri -> uploadPost(uri.toString(), postModel,
                        aVoid -> onUploadSuccessListener.onUploadSuccess(postModel.getId())));
    }

    private static void uploadImages(List<String> imageList, String filename, OnSuccessListener<Uri> s) {

        for (int i = 0; i < imageList.size(); i++) {
            StorageReference reference = FirebaseStorage.getInstance()
                    .getReference().child("post_img/" + filename + "-" + i);

            Uri uri = Uri.parse(imageList.get(i));
            UploadTask uploadTask = reference.putFile(uri);

            uploadTask.continueWithTask(task -> {
                if (task.isSuccessful())
                    return reference.getDownloadUrl();

                onUploadFailureListener.onUploadFailed(
                        FirebaseErrorUtil.getErrorMessage(task.getException(), "이미지 업로드에 실패하였습니다."));
                return null;
            })
                    .addOnSuccessListener(s)
                    .addOnFailureListener(e -> onUploadFailureListener.onUploadFailed(
                            FirebaseErrorUtil.getErrorMessage(e, "이미지 주소를 가져오는 데 실패했습니다.")));
        }
    }

    private static void uploadPost(String url, PostModel postModel, OnSuccessListener<Void> s) {
        // url 추가해줌
        urlList.add(url);
        if (urlList.size() < postModel.getImages().size())
            return;

        // urlList의 개수가 이미지 개수보다 크거나 같으면
        // postModel의 이미지를 url 링크로 바꿈
        postModel.setImages(urlList);

        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .document(postModel.getId())
                .set(postModel)
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onUploadFailureListener.onUploadFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "업로드에 실패하였습니다.")));
    }

    private static boolean checkImagesExtension(List<String> imageList) {
        for (String uri : imageList) {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());

            if (!mimeType.equals("image/jpeg") && !mimeType.equals("image/png"))
                return false;
        }
        return true;
    }
}
