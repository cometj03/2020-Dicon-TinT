package com.sunrin.tint.Screen.Posting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sunrin.tint.R;
import com.sunrin.tint.Util.CreateUtil;

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
