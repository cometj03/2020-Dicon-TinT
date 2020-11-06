package com.sunrin.tint.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.sunrin.tint.R;
import com.sunrin.tint.User_Info;

public class ProfileFragment extends Fragment {

    Context mContext;

    ImageView settingBtn;
    TextView nickNameTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        settingBtn = view.findViewById(R.id.settingBtn);
        nickNameTextView = view.findViewById(R.id.nickNameTextView);

        settingBtn.setOnClickListener(v -> {
            //FirebaseAuth.getInstance().signOut();
            Toast.makeText(mContext, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
