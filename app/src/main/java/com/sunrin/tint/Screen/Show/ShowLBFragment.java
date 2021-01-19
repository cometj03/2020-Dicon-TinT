package com.sunrin.tint.Screen.Show;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.sunrin.tint.Firebase.DownLoad.FirebaseLoadOneLB;
import com.sunrin.tint.Models.LookBookModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.DateUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowLBFragment extends Fragment {

    Context mContext;

    private final String lookBookID;
    private TextView lb_userName, lb_timeInterval;
    private CircleImageView lb_userProfile;
    private ImageView lb_mainImage;

    public ShowLBFragment(String lookBookID) {
        this.lookBookID = lookBookID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_lookbook, container, false);

        lb_userName = view.findViewById(R.id.lb_userName);
        lb_timeInterval = view.findViewById(R.id.lb_timeInterval);
        lb_userProfile = view.findViewById(R.id.lb_userProfile);
        lb_mainImage = view.findViewById(R.id.lb_mainImage);

        FirebaseLoadOneLB
                .LoadLookBook(lookBookID,
                        this::setAll,   // Same as 'lookBook -> setAll(lookBook);'
                        errorMsg -> Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show());

        return view;
    }

    private void setAll(LookBookModel lookBook) {
        lb_userName.setText(lookBook.getUserName());
        lb_timeInterval.setText(DateUtil.getTimeAgo(lookBook.getDate(), mContext.getResources()));

        // TODO: Create class to get user profile

        Glide.with(mContext)
                .load(lookBook.getMainImage())
                .error(R.drawable.post_image_empty)
                .into(lb_mainImage);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
