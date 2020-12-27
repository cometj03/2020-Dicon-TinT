package com.sunrin.tint.Screen.Profile;

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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.sunrin.tint.Model.PostModel;
import com.sunrin.tint.R;

import java.util.ArrayList;
import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ItemViewHolder> implements Filterable {

    private List<PostModel> mData, mDataFiltered;
    Context mContext;

    public ProfilePostAdapter() {
        this.mData = new ArrayList<>();
        this.mDataFiltered = new ArrayList<>();
    }

    public void setList(List<PostModel> list) {
        this.mData = list;
        if(mDataFiltered == null || mDataFiltered.isEmpty())
            mDataFiltered = mData;
    }

    public List<PostModel> getList(){ return mDataFiltered; }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.profile_post_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        PostModel item = mDataFiltered.get(position);

        if(!item.getId().isEmpty() && !item.getImages().isEmpty()) {
            // 정사각형으로 잘라서 보여줌
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop());
            Glide.with(holder.thumbNail)
                    .load((item.getImages().get(0)))
                    .apply(requestOptions)
                    .into(holder.thumbNail);
        }
    }

    @Override
    public int getItemCount() {
        if(mDataFiltered ==null)
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
                                    if (!p.getFilters().contains(com.sunrin.tint.Filter.valueOf(s)))
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
            }
        };
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, View cover, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.itemClickListener = mListener;
    }

    private OnItemClickListener itemClickListener;

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbNail;
        View cover;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbNail = itemView.findViewById(R.id.post_thumb_nail);
            cover = itemView.findViewById(R.id.cover);

            thumbNail.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    if (itemClickListener != null)
                        itemClickListener.OnItemClick(v, cover, pos);
            });
        }
    }
}
