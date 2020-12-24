package com.sunrin.tint.MainScreen.Feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.PostViewActivity;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.FirebaseLoadPost;
import com.sunrin.tint.Util.SharedPreferenceUtil;
import com.sunrin.tint.Util.UserCache;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FeedFragment extends Fragment {

    Context mContext;
    private final int[] filterNames = {
            R.string.filter_name1, R.string.filter_name2, R.string.filter_name3, R.string.filter_name4, R.string.filter_name5 };
    private List<Chip> chips = new ArrayList<>();
    private List<Boolean> chipsBooleans = new ArrayList<>(5);

    // RecyclerView Item Data
    List<PostModel> postModelList = new ArrayList<>();

    ChipGroup chipGroup;
    HorizontalScrollView scrollView;
    ImageButton filterToggle;

    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    FeedAdapter adapter;

    // chip 클릭 리스너 생성
    private CompoundButton.OnCheckedChangeListener chipChangeListener = (CompoundButton buttonView, boolean isChecked) -> {
        int tag = (int) buttonView.getTag();
        chipsBooleans.set(tag, isChecked);
        SharedPreferenceUtil.setPrefFilterBool(mContext, chipsBooleans);
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        init(view);

        Button button = view.findViewById(R.id.logoutBtn);
        button.setOnClickListener(view1 -> {
            UserCache.logout(mContext);
            Toast.makeText(mContext, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 새로 고침 코드 작성
                getData();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated: *******");

        //***** Chip Toggle *****//
        filterToggle.setOnClickListener(v -> {
            if (scrollView.getVisibility() == View.VISIBLE)
                scrollView.setVisibility(View.INVISIBLE);
            else
                scrollView.setVisibility(View.VISIBLE);
        });

        //***** RecyclerView *****//
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new FeedAdapter();
        recyclerView.setAdapter(adapter);
        getData();

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, new LinearLayoutManager(mContext).getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);
        // 아이템 간 간격 조정
        VerticalSpaceDecoration itemDecoration = new VerticalSpaceDecoration(20);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;   // null 이라면 강제종료
                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                if (lastVisible >= totalItemCount - 3) {
                    // 마지막 아이템을 보고있음 -> 아이템 추가
                    //feedItemData.add(new FeedItem(FeedItem.Filter.eMakeUp, null, null, "Title example", "subTitle example", "6 hours ago", "userName", ""));
                    //adapter.notifyDataSetChanged();
                }
            }
        });
        // 피드 아이템 클릭 리스너 구현
        adapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
            @SuppressLint("NonConstantResourceId")  // 밑에 사용할 아이디가 유효한지 검사해줌
            @Override
            public void OnItemClick(View v, int position) {
                switch (v.getId()) {
                    case R.id.feed_share:
                        // 공유버튼
                        Toast.makeText(mContext, "Share", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.feed_comment:
                        // 댓글버튼
                        Toast.makeText(mContext, "Comment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.feed_storageBox:
                        // 보관하기 버튼
                        Toast.makeText(mContext, "StorageBox", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.feed_img:
                        // 이미지 클릭
                        // 몀시적 인텐트에 FeedData 객체 담아서 보내기
                        Intent intent = new Intent(mContext, PostViewActivity.class);
                        intent.putExtra("item", postModelList.get(position));
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ********");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ******");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: **********");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: *******");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void init(View view) {
        chipsBooleans = SharedPreferenceUtil.getPrefFilterBool(mContext);
        if (chipsBooleans.isEmpty())
            for (int i = 0; i < 5; i++)
                chipsBooleans.add(false);

        chipGroup = view.findViewById(R.id.chipGroup);
        scrollView = view.findViewById(R.id.scrollView);
        filterToggle = view.findViewById(R.id.filterToggle);
        refreshLayout = view.findViewById(R.id.swipeLayout);
        recyclerView = view.findViewById(R.id.recyclerView);

        chips.add(view.findViewById(R.id.chip1));
        chips.add(view.findViewById(R.id.chip2));
        chips.add(view.findViewById(R.id.chip3));
        chips.add(view.findViewById(R.id.chip4));
        chips.add(view.findViewById(R.id.chip5));
        for (int i = 0; i < 5; i++) {
            chips.get(i).setTag(i);
            chips.get(i).setOnCheckedChangeListener(chipChangeListener);
            chips.get(i).setChecked(chipsBooleans.get(i));
        }
    }

    private void getData()
    {
        FirebaseLoadPost
                .LoadPosts(
                        postModels -> {
                            if (adapter != null) {
                                postModelList = postModels;
                                adapter.setList(postModelList);
                                adapter.notifyDataSetChanged();
                            }
                            // 새로 고침 완료
                            refreshLayout.setRefreshing(false);
                            },
                        errorMsg -> Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show());
    }


    // 아이템 끼리 간격 주기 위한 클래스
    static class VerticalSpaceDecoration extends RecyclerView.ItemDecoration {

        private final int interval;

        VerticalSpaceDecoration(int interval) {
            this.interval = interval;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)
                outRect.bottom = interval;
        }
    }
//    private void spawnFilterChip() {
//        for (int i = 0; i < 5; i++) {
//            Chip chip = new Chip(mContext);
//            chip.setText(getString(filterNames[i]));
//            chip.setCheckable(true);
//
//            // chip.setCheckedIconVisible(false);
//            chipGroup.addView(chip);
//        }
//    }
}
