package com.pp.gridview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pp.gridview.R;
import com.pp.gridview.data.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {
    private final List<ItemModel> dataList = new ArrayList<>();
    private final Context context;
    private int mFromPos = -1;
    private int mToPos = -1;
    private RequestOptions requestOptions = new RequestOptions();

    public BaseAdapter(Context context, List<ItemModel> data) {
        this.context = context;
        setData(data);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.mipmap.ic_launcher_round)
                .skipMemoryCache(false)
                .placeholder(R.mipmap.ic_launcher);
    }

    public void setData(List<ItemModel> data) {
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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ItemModel itemModel = dataList.get(position);
        if (null != itemModel) {
            holder.iv_show.setImageResource(itemModel.getIconId());
        }
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

        private final ImageView iv_show;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_show = itemView.findViewById(R.id.iv_show);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void swapItem(int fromPos, int toPos) {
        ItemModel from = dataList.get(fromPos);
        ItemModel to = dataList.set(toPos, from);
        dataList.set(fromPos, to);

        notifyItemChanged(fromPos);
        notifyItemChanged(toPos);
    }

    public void itemMoved(int fromPos, int toPos) {
        ItemModel from = dataList.get(fromPos);
        ItemModel to = dataList.set(toPos, from);
        dataList.set(fromPos, to);
        notifyItemMoved(fromPos, toPos);
    }
}
