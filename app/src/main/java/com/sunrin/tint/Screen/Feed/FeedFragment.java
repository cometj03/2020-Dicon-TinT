package com.sunrin.tint.Screen.Feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.okdroid.checkablechipview.CheckableChipView;
import com.google.android.material.chip.ChipGroup;
import com.sunrin.tint.Filter;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.Screen.ShowPostActivity;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.FirebaseLoadPost;
import com.sunrin.tint.Util.UserCache;
import com.sunrin.tint.View.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FeedFragment extends Fragment {

    Context mContext;
    private List<CheckableChipView> chipViews = new ArrayList<>();
    private boolean timeout, allLoaded;

    ChipGroup chipGroup;
    HorizontalScrollView chipContainer;
    ImageButton filterToggle;
    ViewGroup emptyView;    // recyclerview에 아무것도 없을 때

    SwipeRefreshLayout refreshLayout;
    ShimmerRecyclerView shimmerRecyclerView;
    RecyclerView recyclerView;
    FeedAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        init(view);

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

        //***** RecyclerView *****//
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setReverseLayout(true);   // 아이템끼리 겹치는 순서를 바꾸기 위해서
//        layoutManager.setStackFromEnd(true);
        shimmerRecyclerView.setLayoutManager(layoutManager);
        adapter = new FeedAdapter(emptyView);
        shimmerRecyclerView.setAdapter(adapter);
        getData();

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

                /*int currentFirstVisible = layoutManager.findFirstVisibleItemPosition();

                if (currentFirstVisible > firstVisible) {
                    // Scrolling up
                    chipContainer.setVisibility(View.INVISIBLE);
                } else {
                    // Scrolling Down
                    chipContainer.setVisibility(View.VISIBLE);
                }

                firstVisible = currentFirstVisible;*/

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
                    onClickStorage(adapter.getList().get(position));
                    break;
                case R.id.feed_img:
                    // 이미지 클릭
                    // 몀시적 인텐트에 PostModel 객체 담아서 보내기
                    Intent intent = new Intent(mContext, ShowPostActivity.class);
                    intent.putExtra("item", adapter.getList().get(position));
                    startActivity(intent);
                    break;
            }
        });
    }

    private void init(View view) {
        chipGroup = view.findViewById(R.id.chipGroup);
        chipContainer = view.findViewById(R.id.scrollView);
        filterToggle = view.findViewById(R.id.filterToggle);
        refreshLayout = view.findViewById(R.id.swipeLayout);
        // recyclerView = view.findViewById(R.id.recyclerView);
        shimmerRecyclerView = view.findViewById(R.id.shimmerRecyclerView);
        chipViews.add(view.findViewById(R.id.chip1));
        chipViews.add(view.findViewById(R.id.chip2));
        chipViews.add(view.findViewById(R.id.chip3));
        chipViews.add(view.findViewById(R.id.chip4));
        chipViews.add(view.findViewById(R.id.chip5));
        emptyView = view.findViewById(R.id.recycler_empty_view);

        for (CheckableChipView chip : chipViews)
            chip.setOnCheckedChangeListener((chipView, aBoolean) -> {
                chip.setCheckedColor(ContextCompat.getColor(mContext,
                        aBoolean ? R.color.pink_700 : R.color.gray));
                updateList();
                return null;
            });

        List<Filter> userFilters = UserCache.getUser(mContext).getUserFilters();
        for (Filter f : userFilters) {
            chipViews.get(f.ordinal()).setCheckedAnimated(true, () -> {return null;});
        }
    }

    private void onClickStorage(PostModel postModel) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("보관함").setMessage("'" + postModel.getTitle() + "' 을(를) 보관함에 추가하시겠습니까?");

        builder.setPositiveButton("확인", (dialog, which) -> {
            LoadingDialog loadingDialog = new LoadingDialog(mContext);
            loadingDialog.setMessage("사용자 정보 업데이트 중...").show();

            UserCache.updateUser(mContext, postModel.getId(), null, UserCache.UPDATE_STORAGE,
                    aVoid -> loadingDialog.setMessage("업데이트 성공!").finish(true),
                    errMsg -> loadingDialog.setMessage(errMsg).finish(false));
        });

        builder.setNegativeButton("취소", (dialog, which) -> {});

        builder.setCancelable(true).show();
    }

    private List<Filter> getFilters() {
        return new ArrayList<Filter>() {
            {
                for (int i = 0; i < chipViews.size(); i++)
                    if (chipViews.get(i).isChecked() && i < Filter.values().length)
                        add(Filter.values()[i]);
            }
        };
    }

    private void updateList() {
        // 필터들을 문자열로 변환한 후 업데이트
        StringBuilder key = new StringBuilder();
        String regex = "";

        for (Filter filter : getFilters()) {
            key.append(regex);
            key.append(filter.toString());
            regex = ":";
        }

        if (adapter != null)
            adapter.getFilter().filter(key);
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
        }, 500);

        FirebaseLoadPost
                .LoadPosts(
                        postModels -> {
                            if (adapter != null) {
                                allLoaded = true;
                                if (timeout)
                                    shimmerRecyclerView.hideShimmerAdapter();

                                adapter.setList(postModels);
                                updateList();
                            }
                            },
                        errorMsg -> Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show());
    }

    public void GotoTOP() {
        shimmerRecyclerView.smoothScrollToPosition(0);
        getData();
    }


    @Override
    public void onPause() {
        super.onPause();
        // onPause 에 주어진 시간이 짧아서 데이터 저장에 용이하지 않음
    }

    @Override
    public void onStop() {
        super.onStop();
        // onStop 에서 데이터 변경사항 저장
        UserCache.updateUser(mContext, null, getFilters(), UserCache.UPDATE_FILTERS,
                aVoid -> {},
                errMsg -> Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
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
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                // outRect.bottom = interval;
                outRect.set(0, interval, 0, 0);
            }
        }
    }
}
