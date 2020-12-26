package com.sunrin.tint.Screen.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sunrin.tint.R;

public class ProfileFragment extends Fragment {

    Context mContext;

    ImageView settingBtn;
    TextView nickNameTextView;


    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

//        settingBtn = view.findViewById(R.id.settingBtn);
//        nickNameTextView = view.findViewById(R.id.nickNameTextView);
//
//        settingBtn.setOnClickListener(v -> {
//            //FirebaseAuth.getInstance().signOut();
//            Toast.makeText(mContext, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
//        });

        btn = view.findViewById(R.id.popupmenu_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup= new PopupMenu(getActivity(), v);//v는 클릭된 뷰를 의미

                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.popup_makeup:
                                Toast.makeText(getActivity(),"popup_makeup",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.popup_hair:
                                Toast.makeText(getActivity(),"popup_hair",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.popup_fashion:
                                Toast.makeText(getActivity(),"popup_fashion",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.popup_nail:
                                Toast.makeText(getActivity(),"popup_nail",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });

                popup.show();//Popup Menu 보이기
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
