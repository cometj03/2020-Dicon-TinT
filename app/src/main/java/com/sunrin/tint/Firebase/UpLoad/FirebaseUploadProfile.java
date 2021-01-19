package com.sunrin.tint.Firebase.UpLoad;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sunrin.tint.Util.FirebaseErrorUtil;

public class FirebaseUploadProfile {

    // TODO: Decrease image size

    private static OnUploadFailureListener onUploadFailureListener;

    public interface OnUploadSuccessListener {
        void onSuccess(String profileLink);
    }

    public interface OnUploadFailureListener {
        void onFailed(String errMsg, boolean skip);
    }

    public static void Upload(Uri profile, String name, String status, OnUploadSuccessListener s, OnUploadFailureListener f) {
        onUploadFailureListener = f;

        if (profile != null && !checkImageExtension(profile.toString())) {
            onUploadFailureListener.onFailed("올바른 형식의 이미지를 업로드 해주세요 (jpeg/png)", false);
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            onUploadFailureListener.onFailed("유저 정보를 찾을 수 없습니다. 프로필 설정을 스킵해주세요.", true);
            return;
        }

        String email = user.getEmail();
        if (profile != null) {
            uploadProfile(email, profile,
                    uri -> updateUserData(email, uri.toString(), name, status,
                            a -> s.onSuccess(uri.toString())));
        } else {
            updateUserData(email, name, status,
                    a -> s.onSuccess(null));
        }
    }

    private static void uploadProfile(String email, Uri profile, OnSuccessListener<Uri> s) {
        StorageReference reference = FirebaseStorage.getInstance()
                .getReference().child("profile/" + email);

        UploadTask uploadTask = reference.putFile(profile);

        // 업로드 후 다운로드 링크 가져옴
        uploadTask.continueWithTask(task -> {
            if (task.isSuccessful())
                return reference.getDownloadUrl();

            onUploadFailureListener.onFailed(
                    FirebaseErrorUtil.getErrorMessage(task.getException(), "이미지 업로드에 실패하였습니다."), false);
            return null;
        })
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onUploadFailureListener.onFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "이미지 주소를 가져오는 데 실패했습니다."), false));
    }

    private static void updateUserData(String email, String profile, String name, String status, OnSuccessListener<Void> s) {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(email)
                .update("profile", profile, "name", name, "status", status)
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onUploadFailureListener.onFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "유저 데이터 업로드에 실패했습니다."), false));
    }

    private static void updateUserData(String email, String name, String status, OnSuccessListener<Void> s) {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(email)
                .update("name", name, "status", status)
                .addOnSuccessListener(s)
                .addOnFailureListener(e -> onUploadFailureListener.onFailed(
                        FirebaseErrorUtil.getErrorMessage(e, "유저 데이터 업로드에 실패했습니다."), false));
    }

    private static boolean checkImageExtension(String uri) {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());

        return mimeType.equals("image/jpeg") || mimeType.equals("image/png");
    }
}
