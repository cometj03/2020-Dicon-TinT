package com.sunrin.tint;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.InputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PostActivity extends AppCompatActivity {
    private FirebaseUser user;
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //private ImageButton image_btn;
    private ArrayList<ClipData.Item> items = new ArrayList<>();
    FirebaseFirestore database;

    Bitmap image;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override //onCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button post_btn = findViewById(R.id.post);
        ImageView image_btn = findViewById(R.id.imgBtn);
        //post_btn.setOnClickListener(view -> Post());

        imageView = findViewById(R.id.imgBtn);

        //imgView 클릭 시 사진 권한 및 가져오기
        imageView.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(PostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(PostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    String[] strings = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(PostActivity.this, strings, 1);

                } else {
                    String[] strings = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(PostActivity.this, strings, 1);
                    Toast.makeText(getApplicationContext(), "권한 허용이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });



    }

    @Override //imgView에 쓰이는 함수
    protected void onActivityResult(int RequestCode, int ResultCode, Intent data) {
        super.onActivityResult(RequestCode, ResultCode, data);
        if (RequestCode == GET_GALLERY_IMAGE) {
            try {
                InputStream i = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(i);
                i.close();
                imageView.setImageBitmap(img);
                image = img;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        if (RequestCode == GET_GALLERY_IMAGE && ResultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri selectedImageUri = data.getData();
//            imageView.setImageURI(selectedImageUri);
//
//        }
    }

    @Override //img권한 가져오기
    public void onRequestPermissionsResult(int requestCode, String permissons[], int[] grantresults){
        switch (requestCode){
            case 1: {
                if(grantresults.length > 0 && grantresults[0] == PackageManager.PERMISSION_GRANTED){
                    //StartActivity(GalleryActivity.class);
                    Log.e("실행 성공","success");
                } else {
                    Toast.makeText(getApplicationContext(),"권한이 필요합니다",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
  
    private void Post() { //등록 버튼
        final String title = ((EditText)findViewById(R.id.title)).getText().toString();
        final String contents = ((EditText)findViewById(R.id.content)).getText().toString();
        //final ImageView imageView = findViewById(R.id.img_btn).get

        if(title.length() > 0 && contents.length() > 0){
            /*user = FirebaseAuth.getInstance().getCurrentUser();
            Post_content post_content = new Post_content(title,contents, user.getUid());
            register(post_content);*/
            firebaseFirestore
                    .collection("posts")
                    .document("test")
                    .set(new Post_content(title, contents, "test user"))
                    .addOnSuccessListener(command -> Toast.makeText(this, "올리기 성공", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(command -> Toast.makeText(this, "올리기 실패", Toast.LENGTH_SHORT).show());
        }

       // imgRegister();




    }

//    private void register(Post_content post_content){ //제목 내용 등록
//        database = FirebaseFirestore.getInstance();
//        database.collection("posts").add(post_content)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                    }
//                });
//
//
//    }
//
//    private void Get_post(){
//        items.clear();
//        database.collection("posts")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if(task.isSuccessful()){
//                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                            Post_content item = documentSnapshot.toObject(Post_content.class);
//                        }
//                    }
//                });
//    }


//    private void imgRegister(Bitmap bitmap) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReferenceFromUrl("gs://you_firebase_app.appspot.com");
//        StorageReference imagesRef = storageRef.child("images/name_of_your_image.jpg");
//
//        UploadTask uploadTask = imagesRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                // Do what you want
//            }
//        });
//    }

}
