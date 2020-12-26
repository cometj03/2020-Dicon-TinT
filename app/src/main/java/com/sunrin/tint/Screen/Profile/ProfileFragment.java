package com.sunrin.tint.Screen.Profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.sunrin.tint.Model.UserModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.UserCache;

public class ProfileFragment extends Fragment {

    Context mContext;

    TextView tv_username;
    ImageButton btn_addLookBook, btn_addPost;
    Button btn_filterMenu, btn_storage, btn_logout;

    UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);

        userModel = UserCache.getUser(mContext);

        tv_username.setText(userModel.getName());


        btn_storage.setOnClickListener(v -> Toast.makeText(mContext, "Storage", Toast.LENGTH_SHORT).show());
        btn_logout.setOnClickListener(v -> logout());

        btn_filterMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getActivity(), v);//v는 클릭된 뷰를 의미

            popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.popup_all:
                        break;
                    case R.id.popup_makeup:
                        break;
                    case R.id.popup_hair:
                        break;
                    case R.id.popup_fashion:
                        break;
                    case R.id.popup_nail:
                        break;
                    case R.id.popup_diet:
                        break;
                    default:
                        break;
                }
                return true;
            });
            popup.show();//Popup Menu 보이기
        });

        return view;
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("로그아웃").setMessage("정말 로그아웃 하시겠습니까?");
        builder.setPositiveButton("로그아웃", (dialog, which) -> {
            UserCache.logout(mContext);
            Toast.makeText(mContext, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        });
        builder.setNegativeButton("취소", (dialog, which) -> {});
        builder.setCancelable(true).show();
    }

    private void init(View view) {
        tv_username = view.findViewById(R.id.tv_username);
        btn_filterMenu = view.findViewById(R.id.popupmenu_btn);
        btn_storage = view.findViewById(R.id.btn_storage);
        btn_logout = view.findViewById(R.id.btn_setting);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
