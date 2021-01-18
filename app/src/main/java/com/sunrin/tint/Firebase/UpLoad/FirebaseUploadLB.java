package com.sunrin.tint.Firebase.UpLoad;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sunrin.tint.Models.LookBookModel;
import com.sunrin.tint.Models.UserModel;
import com.sunrin.tint.Util.DateUtil;
import com.sunrin.tint.Util.FirebaseErrorUtil;
import com.sunrin.tint.Util.UserCache;

public class FirebaseUploadLB {

    // LB : LookBook

    private static FirebaseUploadPost.OnUploadFailureListener onUploadFailureListener;

    public static void Upload(Context context, LookBookModel lookBookModel, FirebaseUploadPost.OnUploadSuccessListener s, FirebaseUploadPost.OnUploadFailureListener f) {
        onUploadFailureListener = f;

        String id = DateUtil.getFileNameWithDate();
        lookBookModel.setId("LB_" + id);

        UserModel user = UserCache.getUser(context);

        if (user == null) {
            onUploadFailureListener.onUploadFailed("유저 정보를 찾을 수 없습니다.");
            return;
        }
        lookBookModel.setUserName(user.getName());
        lookBookModel.setUserEmail(user.getEmail());

        if (lookBookModel.getMainImage() != null && !checkImageExtension(lookBookModel.getMainImage())) {
            onUploadFailureListener.onUploadFailed("올바른 형식의 이미지를 업로드 해주세요 (jpeg/png)");
            return;
        }

        if (user.getPostID().isEmpty()) {
            onUploadFailureListener.onUploadFailed("먼저 게시물을 하나 이상 등록해주세요!");
            return;
        }

        if (lookBookModel.getLinkList().isEmpty()) {
            onUploadFailureListener.onUploadFailed("게시물을 하나 이상 선택해주세요.");
            return;
        }

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
