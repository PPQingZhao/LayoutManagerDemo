package com.pp.gridview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pp.gridview.R;
import com.pp.gridview.customview.gridview.GridItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder>
        implements GridItemTouchHelperCallback.OnItemTouchHelperListener {
    private final List<String> dataList = new ArrayList<>();
    private final Context context;
    private int mFromPos = -1;
    private int mToPos = -1;

    public BaseAdapter(Context context, List<String> data) {
        this.context = context;
        setData(data);
    }

    public void setData(List<String> data) {
        dataList.clear();
        dataList.addAll(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String content = dataList.get(position);
        holder.tv_show.setText(content);
        if (null != onItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == dataList ? 0 : dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_show;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_show = itemView.findViewById(R.id.tv_show);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public void move(RecyclerView.ViewHolder dragHolder, int fromPos, int toPos) {
        if (null != dragHolder) {
            dragHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        swapItem(fromPos, toPos);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(Color.RED);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (null == viewHolder) return;
        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
    }

    public void swapItem(int fromPos, int toPos) {
        Log.e("TAG","*************** fromPos: " + fromPos);
        Log.e("TAG","*************** toPos: " + toPos);
        String from = dataList.get(fromPos);
        String to = dataList.set(toPos, from);
        dataList.set(fromPos, to);
        notifyItemChanged(fromPos);
        notifyItemChanged(toPos);
//        notifyDataSetChanged();
    }
}
