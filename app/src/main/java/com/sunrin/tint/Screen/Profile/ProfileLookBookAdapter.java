package com.sunrin.tint.Screen.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.sunrin.tint.Models.LookBookModel;
import com.sunrin.tint.R;

import java.util.ArrayList;
import java.util.List;


public class ProfileLookBookAdapter extends RecyclerView.Adapter<ProfileLookBookAdapter.ItemViewHolder> implements Filterable {

    Context mContext;
    private List<LookBookModel> mData, mDataFiltered;

    ViewGroup emptyView;

    public ProfileLookBookAdapter(ViewGroup emptyView) {
        this.emptyView = emptyView;
        this.mData = new ArrayList<>();
        this.mDataFiltered = new ArrayList<>();
    }

    public void setList(List<LookBookModel> list){
        this.mData = list;
        if(mDataFiltered == null || mDataFiltered.isEmpty())
            mDataFiltered = mData;

        if (emptyView != null)
            if (getItemCount() <= 0)
                emptyView.setVisibility(View.VISIBLE);
            else
                emptyView.setVisibility(View.GONE);
    }

    public List<LookBookModel> getList(){ return mDataFiltered; }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                /*String[] keys = constraint.toString().split(":");

                if(constraint.toString().length() <=0){
                    mDataFiltered = mData;
                }
                else{
                    mDataFiltered = new ArrayList<LookBookModel>(){
                        {
                            for(LookBookModel m: mData){
                                boolean flag = true;

                                for(String s: keys){
                                    if(!m.getFilters().contains(com.sunrin.tint.Filter.valueOf(s)))
                                        flag = false;
                                }
                                if(flag) add(m);
                            }
                        }
                    };
                }*/

                FilterResults results = new FilterResults();
                results.values = mDataFiltered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataFiltered = (List<LookBookModel>) results.values;
                notifyDataSetChanged();

                if (emptyView != null)
                    if (getItemCount() <= 0)
                        emptyView.setVisibility(View.VISIBLE);
                    else
                        emptyView.setVisibility(View.GONE);
            }
        };
    }

    public interface OnItemClickListener{
        void OnItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.itemClickListener = mListener;
    }

    private OnItemClickListener itemClickListener;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.profile_lookbook_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        LookBookModel item = mDataFiltered.get(position);

        if(!item.getId().isEmpty() && item.getMainImage() != null) {
            // 정사각형으로 잘라서 보여줌
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop());
            Glide.with(holder.thumbNail)
                    .load((item.getMainImage()))
                    .placeholder(R.drawable.post_image_empty)
                    .error(R.drawable.post_image_empty)
                    .apply(requestOptions)
                    .into(holder.thumbNail);
        }
    }

    @Override
    public int getItemCount() {
        if(mDataFiltered == null)
            return 0;
        return mDataFiltered.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbNail;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbNail = itemView.findViewById(R.id.post_thumb_nail);

            thumbNail.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    if (itemClickListener != null)
                        itemClickListener.OnItemClick(v, pos);
            });


        }
    }
}
