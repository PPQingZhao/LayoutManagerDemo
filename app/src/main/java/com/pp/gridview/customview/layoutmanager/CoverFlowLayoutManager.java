package com.pp.gridview.customview.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class CoverFlowLayoutManager extends RecyclerView.LayoutManager {

    private int mTotalHeight;
    private int scrollY;
    private int oldState;
    private int columnCount = 3;
    private int rowCount = 3;
    private boolean canScrollVertically;
    private int pagerIndex;
    private RecyclerView recyclerView;
    private int slidingDirection = NONE_DIRECTION;
    private static final int NONE_DIRECTION = 0;
    private static final int POSITIVE_DIRECTION = 1;
    private static final int NEGATIVE_DIRECTION = -1;


    /**
     * 提供item默认布局参数
     *
     * @return
     */
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 计算item的位置和布局，并根据显示区域回收出界的item
     *
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        detachAndScrapAttachedViews(recycler);
        int width = getWidth();
        int height = getHeight();
        mTotalHeight = 0;
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            View scrapView = recycler.getViewForPosition(i);
            //添加item
            addView(scrapView);
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width / columnCount, View.MeasureSpec.EXACTLY);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height / rowCount, View.MeasureSpec.EXACTLY);
            //测量item
            scrapView.measure(widthMeasureSpec, heightMeasureSpec);
            int decoratedMeasuredWidth = getDecoratedMeasuredWidth(scrapView);
            int decoratedMeasuredHeight = getDecoratedMeasuredHeight(scrapView);
            //计算当前view位置
            int left = i % columnCount * decoratedMeasuredWidth;
            int top = i / rowCount * decoratedMeasuredHeight - scrollY;
            int right = left + decoratedMeasuredWidth;
            int bottom = top + decoratedMeasuredHeight;
            layoutDecorated(scrapView, left, top, right, bottom);
            //计算所有view的总高度

            if (i == itemCount - 1) { //最后一个
                int remainter = itemCount % (columnCount * rowCount);
                if (remainter == 0) {
                } else {
                    mTotalHeight += (columnCount - remainter / columnCount) * decoratedMeasuredHeight;
                }
            } else {
                if (i % columnCount == 0) {
                    mTotalHeight += decoratedMeasuredHeight;
                }
            }
        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //可见区域
        int verticalVisiblityHeight = getVerticalVisiblityHeight();
        //不可见区域
        int verticalGoneHeight = mTotalHeight - verticalVisiblityHeight;
        if (scrollY + dy < 0) { //抵达上边界
            dy = -scrollY;
        } else if (mTotalHeight > verticalVisiblityHeight
                && scrollY + dy > verticalGoneHeight) { //抵达下边界
            dy = verticalGoneHeight - scrollY;
        }
        if (canScrollVertically) {
            offsetChildrenVertical(0 - dy);
            scrollY += dy;
        } else {
            dy = 0;
        }
        if (dy > 0) {
            slidingDirection = POSITIVE_DIRECTION;
        } else if (dy < 0) {
            slidingDirection = NEGATIVE_DIRECTION;
        }
        return dy;
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        recyclerView = null;
    }

    @Override
    public void onAttachedToWindow(final RecyclerView view) {
        super.onAttachedToWindow(view);
        recyclerView = view;
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (null == recyclerView
                || !isAttachedToWindow()) return;
        switch (state) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                canScrollVertically = true;
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                canScrollVertically = oldState == RecyclerView.SCROLL_STATE_IDLE;
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                canScrollVertically = false;
                pagerIndex = scrollY / getHeight();
                int height = recyclerView.getHeight();
                int scrollerY = getScrollY();
                int offest = scrollerY % height;
                Log.e("TAG", "************* offset: " + offest);
                Log.e("TAG", "************* offest > height / 4: " + (offest > height / 4));
                Log.e("TAG", "************* slidingDirection: " + slidingDirection);
                if (offest > 0) {
                    if (slidingDirection == POSITIVE_DIRECTION) { //向下
                        if (offest > height / 4) {
                            recyclerView.smoothScrollBy(0, height - offest);
                        } else {
                            recyclerView.smoothScrollBy(0, -offest);
                        }
                    } else if (slidingDirection == NEGATIVE_DIRECTION) {
                        if (offest < height / 4) {
                            recyclerView.smoothScrollBy(0, height - offest);
                        } else {
                            recyclerView.smoothScrollBy(0, -offest);
                        }
                    }
                }
                break;
        }
        oldState = state;
    }

    @Override
    public void scrollToPosition(int position) {
//        super.scrollToPosition(position);
        Log.e("TAG", "*********** position: " + position);
    }

    public int getPagerIndex() {
        return pagerIndex;
    }

    public void setPagerIndex(int pagerIndex) {
        this.pagerIndex = pagerIndex;
    }

    public int getScrollY() {
        return scrollY;
    }

    /**
     * 竖直方向可见区域
     *
     * @return
     */
    private int getVerticalVisiblityHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

}
