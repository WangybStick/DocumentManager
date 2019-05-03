package com.thundersoft.app.documentmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thundersoft.app.documentmanager.R;
import com.thundersoft.app.documentmanager.bean.CommonBean;

import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private Context mContext;
    private List<CommonBean> mList;
    private boolean mArrangementFlag;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private OnRecyclerViewItemLongListener mOnRecyclerViewItemLongListener;


    public FileListAdapter(Context context, List<CommonBean> list, boolean arrangementFlag) {
        this.mContext = context;
        this.mList = list;
        this.mArrangementFlag = arrangementFlag;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnRecyclerViewItemLongListener mOnItemLongListener;
        private OnRecyclerViewItemClickListener mOnItemClickListener;
        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(@NonNull View itemView, OnRecyclerViewItemClickListener clickListener,
                          OnRecyclerViewItemLongListener longListener) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.fileList_recyclerView_textView);
            mImageView = itemView.findViewById(R.id.fileList_recyclerView_imageView);
            mOnItemClickListener = clickListener;
            mOnItemLongListener = longListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }

        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLongListener != null) {
                mOnItemLongListener.onItemLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (!mArrangementFlag) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_common,
                    viewGroup, false);
        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_common_grid,
                    viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(view, mOnRecyclerViewItemClickListener, mOnRecyclerViewItemLongListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mTextView.setText(mList.get(i).getmFileName());
        viewHolder.mImageView.setImageBitmap(mList.get(i).getmIcon());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);

    }
    public interface OnRecyclerViewItemLongListener{
        void onItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }
    public void setOnItemLongListener(OnRecyclerViewItemLongListener listener){
        this.mOnRecyclerViewItemLongListener =  listener;
    }

    public void removeItem(int position){
        mList.remove(position);
        notifyDataSetChanged();
    }

    public void renameItem(int position,String newName){
        mList.get(position).setmFileName(newName);
        notifyDataSetChanged();
    }

}
