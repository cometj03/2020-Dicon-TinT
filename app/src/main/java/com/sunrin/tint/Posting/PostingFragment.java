package com.sunrin.tint.Posting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sunrin.tint.Post_content;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.TimeAgo;

public class PostingFragment extends Fragment {

    Context mContext;

    Button postBtn;
    EditText titleText, contentText;
    ImageView imageView;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);

        postBtn = view.findViewById(R.id.postBtn);
        titleText = view.findViewById(R.id.titleText);
        contentText = view.findViewById(R.id.contentText);

        postBtn.setOnClickListener(v -> {
            String title = titleText.getText().toString();
            String content = contentText.getText().toString();

            if (title.length() > 0 && content.length() > 0) {
                firebaseFirestore
                        .collection("posts")
                        .document("test")
                        .set(new Post_content(title, content, TimeAgo.getTimeStamp(System.currentTimeMillis())))
                        .addOnSuccessListener(command -> Toast.makeText(mContext, "올리기 성공", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(command -> Toast.makeText(mContext, "올리기 실패", Toast.LENGTH_SHORT).show());
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
