package com.sunrin.tint.Screen.Feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sunrin.tint.Filter;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ItemViewHolder> implements Filterable {

    Context mContext;
    private List<PostModel> mData, mDataFiltered;

    ViewGroup emptyView;

    FeedAdapter(ViewGroup emptyView) {
        this.emptyView = emptyView;
        this.mData = new ArrayList<>();
        this.mDataFiltered = new ArrayList<>();
    }

    public void setList(List<PostModel> list) {
        this.mData = list;
        if (mDataFiltered == null || mDataFiltered.isEmpty())
            mDataFiltered = mData;
    }
    public List<PostModel> getList() {
        return mDataFiltered;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.feed_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        // Bind Data Here
        // TODO: apply animation to views here
        // https://youtu.be/rJ-7KgMAJUo

        PostModel item = mDataFiltered.get(position);

        if (!item.getId().isEmpty()) {
            Glide.with(holder.feed_img)
                    .load(item.getImages().get(0))
                    .into(holder.feed_img);
        }
        holder.title.setText(item.getTitle());
        holder.subTitle.setText(item.getSubTitle());
        holder.timeInterval.setText(DateUtil.getTimeAgo(item.getDate(), mContext.getResources()));
        holder.userName.setText(item.getUserName());

        // Add Chip Filters

        holder.chipGroup.removeAllViews();

        List<String> filterNames = Arrays.asList(mContext.getResources().getStringArray(R.array.FilterNames));
        for (Filter filter : item.getFilters()) {

            View imageHolder = LayoutInflater.from(mContext).inflate(R.layout.feed_item_chip, null);
            TextView chip = imageHolder.findViewById(R.id.chipTextView);
            chip.setText(filterNames.get(filter.ordinal()));    // ordinal : Enum to Int
            // chip.setTextSize(18);

            holder.chipGroup.addView(imageHolder);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataFiltered == null)
            return 0;
        return mDataFiltered.size();
    }

    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // keys : 필터들을 차례로 담은 문자열 -> Filter로 변환
                String[] keys = constraint.toString().split(":");

                if (constraint.toString().length() <= 0) {
                    mDataFiltered = mData;
                } else {
                    // 필터로 찾은 후 반환
                    mDataFiltered = new ArrayList<PostModel>() {
                        {
                            for (PostModel p : mData) {
                                // flag를 사용하여 모든 필터에 들어맞도록 함.
                                boolean flag = true;

                                for (String s : keys) {
                                    if (!p.getFilters().contains(Filter.valueOf(s)))
                                        flag = false;
                                }
                                if (flag) add(p);
                            }
                        }
                    };
                }

                // results에 결과 담고 return
                FilterResults results = new FilterResults();
                results.values = mDataFiltered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // results : return 값
                mDataFiltered = (List<PostModel>) results.values;
                notifyDataSetChanged();

                if (emptyView != null)
                    if (getItemCount() <= 0)
                        emptyView.setVisibility(View.VISIBLE);
                    else
                        emptyView.setVisibility(View.GONE);
            }
        };
    }


    //*****//
    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.itemClickListener = mListener;
    }

    private OnItemClickListener itemClickListener;

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView feed_img, userProfile;
        ImageButton commentBtn, shareBtn, storageBoxBtn;
        TextView title, subTitle, timeInterval, userName;
        ViewGroup chipGroup;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            feed_img = itemView.findViewById(R.id.feed_img);
            userProfile = itemView.findViewById(R.id.feed_userProfile);
            shareBtn = itemView.findViewById(R.id.feed_share);
            commentBtn = itemView.findViewById(R.id.feed_comment);
            storageBoxBtn = itemView.findViewById(R.id.feed_storageBox);
            title = itemView.findViewById(R.id.feed_title);
            subTitle = itemView.findViewById(R.id.feed_subTitle);
            timeInterval = itemView.findViewById(R.id.feed_timeInterval);
            userName = itemView.findViewById(R.id.feed_userName);
            chipGroup = itemView.findViewById(R.id.feed_chipGroup);
            //chipSample = itemView.findViewById(R.id.feed_chipSample);

            //*** Button Listener Setting ***//
            shareBtn.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    if (itemClickListener != null)
                        // 아이템 클릭
                        itemClickListener.OnItemClick(v, pos);
            });
            commentBtn.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    if (itemClickListener != null)
                        itemClickListener.OnItemClick(v, pos);
            });
            storageBoxBtn.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    if (itemClickListener != null)
                        itemClickListener.OnItemClick(v, pos);
            });
            feed_img.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    if (itemClickListener != null)
                        itemClickListener.OnItemClick(v, pos);
            });
        }
    }
}
