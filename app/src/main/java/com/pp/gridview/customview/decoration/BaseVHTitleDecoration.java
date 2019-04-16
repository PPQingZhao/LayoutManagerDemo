package com.pp.gridview.customview.decoration;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.pp.gridview.adapter.BaseTitleMultiItemAdapter;

public class BaseVHTitleDecoration<T extends MultiItemEntity> extends AroundItemDecoration {
    private BaseTitleMultiItemAdapter<T> adapter;
    public BaseVHTitleDecoration(int color, int offsetLeft, int offsetTop, int offsetRight, int offsetBottom) {
        super(color, offsetLeft, offsetTop, offsetRight, offsetBottom);
    }

    public void attachAdapter(BaseTitleMultiItemAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (parent.getChildCount() <= 0) return;
        View childAt = parent.getChildAt(0);
        int position = parent.getChildAdapterPosition(childAt);
        int itemViewType = adapter.getItemViewType(position);
        View titleView = null;
        c.save();
        switch (itemViewType) {
            case BaseTitleMultiItemAdapter.TYPE_LEVEL_0:
                if (childAt.getTop() == 0) return;
                titleView = adapter.creatTitleView(parent, position, BaseTitleMultiItemAdapter.TYPE_LEVEL_0);
                break;
            case BaseTitleMultiItemAdapter.TYPE_LEVEL_1:
                T item = adapter.getItem(position);
                position = adapter.getParentPosition(item);
                titleView = adapter.creatTitleView(parent, position, BaseTitleMultiItemAdapter.TYPE_LEVEL_0);
                break;
        }
        if (null != titleView) {
            c.translate(0, getTitleTop(titleView.getHeight(), parent));
            titleView.draw(c);
        }
        c.restore();
    }

    private float getTitleTop(int titleHeight, RecyclerView parent) {
        float top = 0;
        View childAt = parent.getChildAt(1);
        int position = parent.getChildAdapterPosition(childAt);
        int itemViewType = adapter.getItemViewType(position);
        if (itemViewType == BaseTitleMultiItemAdapter.TYPE_LEVEL_0) {
            if (childAt.getTop() <= titleHeight) {
                top = childAt.getTop() - titleHeight;
            }
        }
        return top;
    }
}
