package com.sunrin.tint.Screen.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.sunrin.tint.Filter;
import com.sunrin.tint.Firebase.DownLoad.FirebaseLoadLBs;
import com.sunrin.tint.Firebase.DownLoad.FirebaseLoadPosts;
import com.sunrin.tint.Models.UserModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Screen.MainActivity;
import com.sunrin.tint.Screen.Show.ShowLBActivity;
import com.sunrin.tint.Screen.Show.ShowPostActivity;
import com.sunrin.tint.Screen.SplashActivity;
import com.sunrin.tint.Screen.StorageActivity;
import com.sunrin.tint.Util.CreateUtil;
import com.sunrin.tint.Util.ImagePickerUtil;
import com.sunrin.tint.Util.SharedPreferenceUtil;
import com.sunrin.tint.Util.UserCache;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment {

    private final String PREF_TITLE = "profile_menu_title";
    private final String PREF_POST_FILTER = "profile_post_filter";

    Context mContext;

    TextView tv_username, tv_status;
    ImageButton btn_addLookBook, btn_addPost, btn_storage, btn_logout;
    Button btn_filterMenu;
    CircleImageView profile;
    ViewGroup emptyView1, emptyView2;

    SwipeRefreshLayout swipeRefreshLayout;
    ShimmerRecyclerView lookBook_Recycler, post_Recycler;
    ProfileLookBookAdapter lookBookAdapter;
    PostGridAdapter postAdapter;

    private UserModel user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);

        user = UserCache.getUser(mContext);

        assert user != null;
        tv_username.setText(user.getName());
        tv_status.setText(user.getStatus());
        Glide.with(profile)
                .load(user.getProfile())
                .placeholder(R.drawable.profile_empty_feed) // 사진이 로딩 되기 전 미리보기 이미지
                .error(R.drawable.profile_empty_feed)       // 사진 불러오지 못했을 때
                .into(profile);

        profile.setOnClickListener(v -> changeProfile());
        btn_storage.setOnClickListener(v -> startActivity(new Intent(mContext, StorageActivity.class)));
        btn_logout.setOnClickListener(v -> logout());
        btn_addLookBook.setOnClickListener(v -> CreateUtil.CreateLookBook(mContext, getActivity()));
        btn_addPost.setOnClickListener(v -> CreateUtil.CreatePost(mContext, getActivity()));

        btn_filterMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getActivity(), v);  //v는 클릭된 뷰를 의미

            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {

                String[] filterNames = getResources().getStringArray(R.array.FilterNames);
                String title = "", filterString = "";

                switch (item.getItemId()) {
                    case R.id.popup_all:
                        title = "전체 ▼";
                        filterString = "ALL";
                        break;
                    case R.id.popup_makeup:
                        title = filterNames[0] + " ▼";
                        filterString = Filter.eMakeUp.toString();
                        break;
                    case R.id.popup_hair:
                        title = filterNames[1] + " ▼";
                        filterString = Filter.eHair.toString();
                        break;
                    case R.id.popup_fashion:
                        title = filterNames[2] + " ▼";
                        filterString = Filter.eFashion.toString();
                        break;
                    case R.id.popup_nail:
                        title = filterNames[3] + " ▼";
                        filterString = Filter.eNail.toString();
                        break;
                    case R.id.popup_diet:
                        title = filterNames[4] + " ▼";
                        filterString = Filter.eDiet.toString();
                        break;
                }
                btn_filterMenu.setText(title);
                SharedPreferenceUtil.setString(mContext, PREF_TITLE, title);
                postAdapter.getFilter().filter(filterString);
                SharedPreferenceUtil.setString(mContext, PREF_POST_FILTER, filterString);

                return true;
            });
            popup.show();   //Popup Menu 보이기
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //***** RecyclerView *****//
        // LookBook
        lookBook_Recycler.showShimmerAdapter();
        lookBook_Recycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        lookBookAdapter = new ProfileLookBookAdapter(emptyView1);
        lookBook_Recycler.setAdapter(lookBookAdapter);
        getLookBookData();

        lookBookAdapter.setOnItemClickListener((v, position) -> {
            Intent intent = new Intent(mContext, ShowLBActivity.class);
            intent.putExtra("click_position", position);
            intent.putStringArrayListExtra("lb_list", (ArrayList<String>) user.getLookBookID());
            startActivity(intent);
        });

        // Post
        post_Recycler.showShimmerAdapter();
        post_Recycler.setLayoutManager(new GridLayoutManager(mContext, 3));
        postAdapter = new PostGridAdapter(emptyView2);
        post_Recycler.setAdapter(postAdapter);
        getPostData();

        postAdapter.setOnItemClickListener((v, cover, position) -> {
            Intent intent = new Intent(mContext, ShowPostActivity.class);
            intent.putExtra("item", postAdapter.getList().get(position));
            startActivity(intent);
        });

        //**** RefreshLayout ****//
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getLookBookData();
            getPostData();
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.pink_700);
    }

    private void getLookBookData() {
        lookBook_Recycler.showShimmerAdapter();

        FirebaseLoadLBs
                .LoadLookBooks(user.getLookBookID(),
                        lookBookModels -> {
                            if (lookBookAdapter != null) {
                                lookBook_Recycler.hideShimmerAdapter();
                                lookBookAdapter.setList(lookBookModels);
                                lookBookAdapter.notifyDataSetChanged();

                                swipeRefreshLayout.setRefreshing(false);    // 로딩 종료
                                Log.e(TAG, "getLookBookData: 룩북 불러옴");
                            }
                        },
                        errorMsg -> Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show());
    }

    private void getPostData() {
        post_Recycler.showShimmerAdapter();

        FirebaseLoadPosts
                .LoadPosts(user.getPostID(),
                        postModels -> {
                            if (postAdapter != null) {
                                post_Recycler.hideShimmerAdapter();
                                postAdapter.setList(postModels);

                                String menuFilter = SharedPreferenceUtil.getString(mContext, PREF_POST_FILTER);
                                postAdapter.getFilter().filter(menuFilter);
                            }
                        },
                        errorMsg -> Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show());
    }

    private void changeProfile() {
        ImagePickerUtil.PickImage(mContext, getActivity(), "Profile", image -> {
            Toast.makeText(mContext, "프로필 변경은 개발중에 있습니다 :)", Toast.LENGTH_SHORT).show();
        });
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("로그아웃").setMessage("정말 로그아웃 하시겠습니까?");
        builder.setPositiveButton("로그아웃", (dialog, which) -> {
            UserCache.logout(mContext);
            Toast.makeText(mContext, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(mContext, SplashActivity.class));
            ((MainActivity) mContext).finish();
        });
        builder.setNegativeButton("취소", (dialog, which) -> {});
        builder.setCancelable(true).show();
    }

    private void init(View view) {
        tv_username = view.findViewById(R.id.tv_username);
        tv_status = view.findViewById(R.id.tv_status);
        btn_filterMenu = view.findViewById(R.id.popupmenu_btn);
        btn_storage = view.findViewById(R.id.btn_storage);
        btn_logout = view.findViewById(R.id.btn_setting);
        btn_addLookBook = view.findViewById(R.id.add_lookbook_btn);
        btn_addPost = view.findViewById(R.id.add_post_btn);
        profile = view.findViewById(R.id.profile_imageview);
        lookBook_Recycler = view.findViewById(R.id.lookbook_recycler);
        post_Recycler = view.findViewById(R.id.post_recycler);
        emptyView1 = view.findViewById(R.id.empty_view1);
        emptyView2 = view.findViewById(R.id.empty_view2);
        swipeRefreshLayout = view.findViewById(R.id.profile_refresh);

        String menuTitle = SharedPreferenceUtil.getString(mContext, PREF_TITLE);
        btn_filterMenu.setText(menuTitle.length() > 0 ? menuTitle : "전체 ▼");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
