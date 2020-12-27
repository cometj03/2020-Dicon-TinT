package com.sunrin.tint.Util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sunrin.tint.Model.LookBookModel;
import com.sunrin.tint.Model.UserModel;

import java.util.List;

import static android.content.ContentValues.TAG;

public class FirebaseUploadLookBook {

    private static FirebaseUploadPost.OnUploadFailureListener onUploadFailureListener;

    public static void Upload(Context context, LookBookModel lookBookModel, FirebaseUploadPost.OnUploadSuccessListener s, FirebaseUploadPost.OnUploadFailureListener f) {
        onUploadFailureListener = f;

        String id = DateUtil.getFileNameWithDate();
        lookBookModel.setId("LB_" + id);

        UserModel userModel = UserCache.getUser(context);
        lookBookModel.setUserName(userModel.getName());
        lookBookModel.setUserEmail(userModel.getEmail());

        if (lookBookModel.getMainImage() != null && !checkImageExtension(lookBookModel.getMainImage())) {
            onUploadFailureListener.onUploadFailed("올바른 형식의 이미지를 업로드 해주세요 (jpeg/png)");
            return;
        }

//        if (links == null || links.isEmpty()) {
//            onUploadFailureListener.onUploadFailed("게시물을 하나 이상 선택해주세요.");
//            return;
//        }

        uploadImage(lookBookModel.getMainImage(), id,
                uri -> uploadLookBook(uri.toString(), lookBookModel,
                        a -> s.onUploadSuccess(lookBookModel.getId())));
    }

    private static void uploadImage(String image, String filename, OnSuccessListener<Uri> s) {
        StorageReference reference = FirebaseStorage.getInstance()
                .getReference().child("lookbooks/" + filename);

        Uri uri = Uri.parse(image);
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

    private static void uploadLookBook(String url, LookBookModel lookBookModel, OnSuccessListener<Void> s) {
        lookBookModel.setMainImage(url);

        FirebaseFirestore
                .getInstance()
                .collection("lookbooks")
                .document(lookBookModel.getId())
                .set(lookBookModel)
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onUploadFailureListener.onUploadFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "업로드에 실패하였습니다.")));
    }

    private static boolean checkImageExtension(String image) {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(image);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());

        return mimeType.equals("image/jpeg") || mimeType.equals("image/png");
    }
}
