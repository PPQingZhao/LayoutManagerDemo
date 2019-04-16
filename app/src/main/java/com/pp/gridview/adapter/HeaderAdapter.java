package com.pp.gridview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pp.gridview.R;
import com.pp.gridview.data.HeaderItemModel;

import java.util.List;

public class HeaderAdapter extends BaseHeaderAdapter<HeaderItemModel, HeaderAdapter.HeaderViewHolder, HeaderAdapter.ViewHolder> {
    private RecyclerView recyclerView;
    private final SparseArray<View> headerSparseArr = new SparseArray<>();

    public HeaderAdapter(Context context, List<HeaderItemModel> data) {
        super(context, data);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View header = headerSparseArr.get(viewType);
        if (null == header) {
            header = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_header, null, false);
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.UNSPECIFIED);
            header.measure(widthMeasureSpec, heightMeasureSpec);
            //只有调用 layout()　方法之后,view才有宽高信息
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
            headerSparseArr.put(viewType, header);
        }
        return new HeaderViewHolder(header);
    }


    @Override
    public void onBindHeaderViewHolder(@NonNull HeaderViewHolder holder, int position) {
        if (position < 0 || position >= getItemCount()) return;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_decorationitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final HeaderItemModel itemModel = dataList.get(position);
        if (null != itemModel) {
            if (itemModel.isContainTitle() && holder.getHeaderViewHolder() == null) {
                HeaderViewHolder headerViewHolder = onCreateHeaderViewHolder(recyclerView, 0);
                onBindHeaderViewHolder(headerViewHolder, position);
                holder.setHeaderViewHolder(headerViewHolder);
            }
            holder.decorationitem_tv.setText(itemModel.getName());
            holder.decorationitem_iv.setImageResource(itemModel.getIconId());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView decorationitem_tv;
        private final ImageView decorationitem_iv;

        public HeaderViewHolder getHeaderViewHolder() {
            return headerViewHolder;
        }

        public void setHeaderViewHolder(HeaderViewHolder headerViewHolder) {
            this.headerViewHolder = headerViewHolder;
        }

        private HeaderViewHolder headerViewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            decorationitem_tv = itemView.findViewById(R.id.decorationitem_tv);
            decorationitem_iv = itemView.findViewById(R.id.decorationitem_iv);
        }
    }

    public class HeaderViewHolder extends BaseHeaderAdapter.BaseHeaderViewHolder {

        private final TextView header_tv;

        public HeaderViewHolder(View header) {
            super(header);
            header_tv = header.findViewById(R.id.header＿tv);
        }
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
