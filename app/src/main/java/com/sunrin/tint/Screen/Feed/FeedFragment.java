package com.sunrin.tint.Screen.Feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.okdroid.checkablechipview.CheckableChipView;
import com.google.android.material.chip.ChipGroup;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.Screen.PostViewActivity;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.FirebaseLoadPost;
import com.sunrin.tint.Util.UserCache;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FeedFragment extends Fragment {

    Context mContext;
    private List<CheckableChipView> chips = new ArrayList<>();
    private static int firstVisible;
    private boolean timeout, allLoaded;

    // RecyclerView Item Data
    List<PostModel> postModelList = new ArrayList<>();

    ChipGroup chipGroup;
    HorizontalScrollView chipContainer;
    ImageButton filterToggle;

    SwipeRefreshLayout refreshLayout;
    ShimmerRecyclerView shimmerRecyclerView;
    RecyclerView recyclerView;
    FeedAdapter adapter;

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

        //***** Chip Toggle *****//
        filterToggle.setOnClickListener(v -> {
            if (chipContainer.getVisibility() == View.VISIBLE)
                chipContainer.setVisibility(View.INVISIBLE);
            else
                chipContainer.setVisibility(View.VISIBLE);
        });


        // 새로 고침 코드 작성
        refreshLayout.setOnRefreshListener(() -> {
            getData();
            // 새로 고침 완료
            refreshLayout.setRefreshing(false);
        });
        // 한 바퀴 돌 때마다 색 바뀜
        refreshLayout.setColorSchemeResources(
                R.color.pink_700
//                android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light,
//                R.color.pink_200
        );

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated: *******");


        //***** RecyclerView *****//
        shimmerRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new FeedAdapter();
        shimmerRecyclerView.setAdapter(adapter);
        getData();

        firstVisible = ((LinearLayoutManager) shimmerRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, new LinearLayoutManager(mContext).getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);
        // 아이템 간 간격 조정
        VerticalSpaceDecoration itemDecoration = new VerticalSpaceDecoration(20);
        shimmerRecyclerView.addItemDecoration(itemDecoration);

        shimmerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;   // null 이라면 강제종료

                int currentFirstVisible = layoutManager.findFirstVisibleItemPosition();

                if (currentFirstVisible > firstVisible) {
                    // Scrolling up
                    chipContainer.setVisibility(View.INVISIBLE);
                } else {
                    // Scrolling Down
                    chipContainer.setVisibility(View.VISIBLE);
                }

                firstVisible = currentFirstVisible;

                /*int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                if (lastVisible >= totalItemCount - 3) {
                    // 마지막 아이템을 보고있음 -> 아이템 추가
                    //feedItemData.add(new FeedItem(FeedItem.Filter.eMakeUp, null, null, "Title example", "subTitle example", "6 hours ago", "userName", ""));
                    //adapter.notifyDataSetChanged();
                }*/
            }
        });

        // 피드 아이템 클릭 리스너 구현
        adapter.setOnItemClickListener((v, position) -> {
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
        chipGroup = view.findViewById(R.id.chipGroup);
        chipContainer = view.findViewById(R.id.scrollView);
        filterToggle = view.findViewById(R.id.filterToggle);
        refreshLayout = view.findViewById(R.id.swipeLayout);
        // recyclerView = view.findViewById(R.id.recyclerView);
        shimmerRecyclerView = view.findViewById(R.id.shimmerRecyclerView);
    }

    private void getData()
    {
        timeout = allLoaded = false;

        // recyclerView loading start
        shimmerRecyclerView.showShimmerAdapter();
        shimmerRecyclerView.postDelayed(() -> {
            timeout = true;
            if (allLoaded)
                shimmerRecyclerView.hideShimmerAdapter();   // recyclerView loading stop
        }, 2300);

        FirebaseLoadPost
                .LoadPosts(
                        postModels -> {
                            if (adapter != null) {
                                allLoaded = true;
                                if (timeout)
                                    shimmerRecyclerView.hideShimmerAdapter();

                                postModelList = postModels;
                                adapter.setList(postModelList);
                                adapter.notifyDataSetChanged();
                            }
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
