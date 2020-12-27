package com.sunrin.tint.Screen.Posting;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.R;

public class PostingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        String[] a = getIntent().getStringArrayExtra("post");
    }
}
