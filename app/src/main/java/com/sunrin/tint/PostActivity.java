package com.sunrin.tint;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PostActivity extends AppCompatActivity {
    private FirebaseUser user;
    private static final String TAG = "PostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button post_btn = findViewById(R.id.post);
        ImageView image_btn = findViewById(R.id.img_btn);

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post();

            }
        });

        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        PostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(PostActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        ActivityCompat.requestPermissions(PostActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        ActivityCompat.requestPermissions(PostActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        Toast.makeText(getApplicationContext(), "권한 허용이 필요합니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //StartActivity(GalleryActivity.class);
                    //Log.e("success","success");
                    Intent intent = new Intent(view.getContext(),GalleryActivity.class);
                    startActivity(intent);


                }
            }

        });


    }

    @Override
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

    private void Post(){
        final String title = ((EditText)findViewById(R.id.title)).getText().toString();
        final String contents = ((EditText)findViewById(R.id.content)).getText().toString();

        if(title.length()>0 && contents.length() > 0){
            user = FirebaseAuth.getInstance().getCurrentUser();
            Post_content post_content = new Post_content(title,contents, user.getUid());
            regisetr(post_content);
        }
    }

    private void regisetr(Post_content post_content){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("posts").add(post_content)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }





}
