package com.example.administrator.jsoupdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dreamY on 2017/6/15.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String>mListTitle = new ArrayList<>();
    private List<String>mListImage = new ArrayList<>();
    private List<String>mListContent = new ArrayList<>();
    private Context mContext;
    private OnClickListener mOnClickListener;
    public interface OnClickListener{
        void click(View mView, int position);
    }
    public  void setOnClickListener(OnClickListener mOnClickListener){
         this.mOnClickListener = mOnClickListener;
    }

    public MyAdapter(List<String> mListTitle, List<String> mListImage, List<String> mListContent,Context mContext) {
        this.mListTitle = mListTitle;
        this.mListImage = mListImage;
        this.mListContent = mListContent;
        this.mContext=mContext;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup mViewGroup, int mI) {
        View mView = LayoutInflater.from(mViewGroup.getContext()).inflate(R.layout.item_layout,mViewGroup,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder mViewHolder, int mI) {
        mViewHolder.mTextView.setText(mListTitle.get(mI));
        Glide.with(mContext)
                .load(mListImage.get(mI))
                .into(mViewHolder.mImageView);
        if (mOnClickListener !=null ){
            mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View mView) {
                    int position = mViewHolder.getLayoutPosition();
                    mOnClickListener.click(mViewHolder.itemView,position);
                }
            }

            );
        }


    }

    @Override
    public int getItemCount() {
        return mListContent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_item);
            mTextView = (TextView) itemView.findViewById(R.id.text_item);
        }
    }
}
