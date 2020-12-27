package com.sunrin.tint.Screen.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sunrin.tint.Model.LookBookModel;
import com.sunrin.tint.R;
import com.sunrin.tint.Util.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProfileLookBookAdapter extends RecyclerView.Adapter<ProfileLookBookAdapter.ItemViewHolder> implements Filterable {

    Context mContext;
    private List<LookBookModel> mData, mDataFiltered;

    ProfileLookBookAdapter(){
        this.mData = new ArrayList<>();
        this.mDataFiltered = new ArrayList<>();
    }

    public void setList(List<LookBookModel> list){
        this.mData = list;
        if(mDataFiltered == null || mDataFiltered.isEmpty())
            mDataFiltered = mData;
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
            }
        };
    }

    public interface OnItemClickListener{
        void OnItemClick(View v, int position);
    }

    public void setItemClickListener(OnItemClickListener mListener){
        this.itemClickListener = mListener;
    }

    private OnItemClickListener itemClickListener;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.profile_item_lookbook_temp, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        LookBookModel item = mDataFiltered.get(position);

        /*if(!item.getId().isEmpty()){
            Glide.with(holder.lookbook_img).load(item.getImage().get(0)).into(holder.lookbook_img);
        }
        holder.contents.setText(item.getContents());
        holder.userName.setText(item.getUserName());
        holder.timeInterval.setText(DateUtil.getTimeAgo(item.getDate(), mContext.getResources()));

        holder.chipGroup.removeAllViews();

        List<String> filterNames = Arrays.asList(mContext.getResources().getStringArray(R.array.FilterNames));
        for (com.sunrin.tint.Filter filter: item.getFilters()) {
            View imageHolder = LayoutInflater.from(mContext).inflate(R.layout.feed_item_chip, null);
            TextView chip = imageHolder.findViewById(R.id.chipTextView);
            chip.setText(filterNames.get(filter.ordinal()));

            holder.chipGroup.addView(imageHolder);
        }*/
    }

    @Override
    public int getItemCount() {
        if(mDataFiltered == null)
            return 0;
        return mDataFiltered.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView lookbook_img, userProfile;
        TextView contents, timeInterval, userName;
        ViewGroup chipGroup;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lookbook_img = itemView.findViewById(R.id.feed_img);
            userProfile = itemView.findViewById(R.id.feed_userProfile);
            contents = itemView.findViewById(R.id.lookbook_contents);
            timeInterval = itemView.findViewById(R.id.feed_timeInterval);
            userName = itemView.findViewById(R.id.feed_userName);
            chipGroup = itemView.findViewById(R.id.feed_chipGroup);


            lookbook_img.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                    if (itemClickListener != null)
                        itemClickListener.OnItemClick(v, pos);
            });


        }
    }
}
