package com.pp.gridview.customview.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pp.gridview.adapter.BaseHeaderAdapter;
import com.pp.gridview.adapter.HeaderAdapter;

public class HeaderDecorration extends AroundItemDecoration {
    private BaseHeaderAdapter adapter;

    public HeaderDecorration(@ColorInt int color,
                             int offsetLeft,
                             int offsetTop,
                             int offsetRight,
                             int offsetBottom) {
        super(color, offsetLeft, offsetTop, offsetRight, offsetBottom);
    }

    public void attchToAdapter(BaseHeaderAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (adapter.isContainTitle(position)) {
            BaseHeaderAdapter.BaseHeaderViewHolder baseHeaderViewHolder = adapter.onCreateHeaderViewHolder(parent, 0);
            outRect.top = mDecorationHelper.getOffsetTop() + baseHeaderViewHolder.header.getHeight();
        }
    }

    /**
     * 这个方法在条目绘制之后调用
     * 适合做粘性标题或者分组标题绘制
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        if (childCount == 0) return;
        for (int i = 0; i < childCount; i++) {
            c.save();
            final View childAt = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(childAt);
            if (adapter.isContainTitle(position)) {
                RecyclerView.ViewHolder childViewHolder = parent.getChildViewHolder(childAt);
                if (childViewHolder instanceof HeaderAdapter.ViewHolder) {
                    HeaderAdapter.HeaderViewHolder headerViewHolder = ((HeaderAdapter.ViewHolder) childViewHolder).getHeaderViewHolder();
                    if (null != headerViewHolder) {
                        c.translate(childAt.getLeft(), childAt.getTop() - headerViewHolder.header.getHeight());
                        //将header画在Canvas上
                        headerViewHolder.header.draw(c);
                    }
                }
            }
            c.restore();
        }
    }
}