package com.example.a301project.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by chxip on 2019/8/21.
 */

public class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<?> dataList;
    public Context context;
    public LayoutInflater layoutInflater;

    public BaseAdapter(List<?> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemDeleteClickListener onItemDeleteClickListener;

    public void setOnItemDeleteClickListener(OnItemDeleteClickListener onItemDeleteClickListener) {
        this.onItemDeleteClickListener = onItemDeleteClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }


    public interface OnItemClickListener {
        void onClickListener(View view, int position);
    }



    public interface OnItemDeleteClickListener {
        void onDeleteClickListener(View view, int position);
    }

    //  删除数据
    public void removeData(int position) {
        dataList.remove(position);
        notifyItemRemoved(position + 1);
        notifyItemRangeChanged(position + 1, dataList.size() - position);
    }

    //  删除数据
    public void removeData(int position,int type) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
    }
    //添加数据
    public void addItem(int position) {
        //dataList.add(position, data);
        notifyItemInserted(position);//通知演示插入动画
        notifyItemRangeChanged(position, dataList.size() - position);//通知数据与界面重新绑定
    }
}
