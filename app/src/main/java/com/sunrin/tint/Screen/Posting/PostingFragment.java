package com.sunrin.tint.Screen.Posting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.okdroid.checkablechipview.CheckableChipView;
import com.sunrin.tint.Filter;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.CreateUtil;
import com.sunrin.tint.Util.FirebaseUploadPost;
import com.sunrin.tint.Util.ImagePickerUtil;
import com.sunrin.tint.Util.UserCache;
import com.sunrin.tint.View.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class PostingFragment extends Fragment {

    Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);

        ImageView imgBtn = view.findViewById(R.id.postBtn);
        imgBtn.setOnClickListener(v -> CreateUtil.CreatePost(mContext, getActivity()));

        ImageButton lookBookBtn = view.findViewById(R.id.lookbookBtn);
        lookBookBtn.setOnClickListener(v -> CreateUtil.CreateLookBook(mContext, getActivity()));

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
