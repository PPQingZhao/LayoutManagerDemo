package com.pp.gridview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.pp.gridview.data.BaseHeaderItem;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseHeaderAdapter<T extends BaseHeaderItem,
        HeadVH extends BaseHeaderAdapter.BaseHeaderViewHolder,
        VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected final List<T> dataList = new ArrayList<>();

    public BaseHeaderAdapter(Context context, List<T> data) {
        setData(data);
    }

    public void setData(List<T> data) {
        dataList.clear();
        dataList.addAll(data);
    }

    @Override
    public int getItemCount() {
        return null == dataList ? 0 : dataList.size();
    }

    public abstract HeadVH onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindHeaderViewHolder(@NonNull HeadVH holder, final int position);

    public class BaseHeaderViewHolder {
        public final View header;

        public BaseHeaderViewHolder(View header) {
            if (header == null) {
                throw new IllegalArgumentException("header may not be null");
            }
            this.header = header;
        }
    }


    public boolean isContainTitle(int position) {
        if (position < 0 || position >= dataList.size()) return false;
        final BaseHeaderItem itemModel = dataList.get(position);
        return null == itemModel ? false : itemModel.isContainTitle();
    }

}
