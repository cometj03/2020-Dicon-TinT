package com.sunrin.tint.Posting;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sunrin.tint.Feed.FeedItem;
import com.sunrin.tint.PostActivity;
import com.sunrin.tint.Post_content;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.TimeAgo;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class PostingFragment extends Fragment {

    Context mContext;
    Activity mActivity;

    Button postBtn;
    EditText titleText, contentText;
    ImageView imgBtn;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference;

    Uri selectedImageUri;

    private final int GET_GALLERY_IMAGE = 200;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);

        postBtn = view.findViewById(R.id.postBtn);
        titleText = view.findViewById(R.id.titleText);
        contentText = view.findViewById(R.id.contentText);
        imgBtn = view.findViewById(R.id.imgBtn);

        storageReference = storage.getReference();

        postBtn.setOnClickListener(v -> {
            String title = titleText.getText().toString();
            String content = contentText.getText().toString();

            if (title.length() > 0 && content.length() > 0) {

                firebaseFirestore
                        .collection("posts")
                        .document("test2")
                        .set(new Post_content(title, content, TimeAgo.getTimeStamp(System.currentTimeMillis())))
                        .addOnSuccessListener(command -> Toast.makeText(mContext, "올리기 성공", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(command -> Toast.makeText(mContext, "올리기 실패", Toast.LENGTH_SHORT).show());
            }

            UploadImage(selectedImageUri);
        });

        //imgView 클릭 시 사진 권한 및 가져오기
        imgBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    String[] strings = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(mActivity, strings, 1);

                } else {
                    String[] strings = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(mActivity, strings, 1);
                    Toast.makeText(mContext, "권한 허용이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imgBtn.setImageURI(selectedImageUri);
            Toast.makeText(mContext, "이미지 추가됨", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadImage(Uri filePath) {

        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(mContext, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
    }
}
