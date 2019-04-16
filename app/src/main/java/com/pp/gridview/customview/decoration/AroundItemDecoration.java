package com.pp.gridview.customview.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class AroundItemDecoration extends RecyclerView.ItemDecoration {

    protected final DecorationHelper mDecorationHelper;

    public AroundItemDecoration(@ColorInt int color,
                                int offsetLeft,
                                int offsetTop,
                                int offsetRight,
                                int offsetBottom) {
        mDecorationHelper = new DecorationHelper(color, offsetLeft, offsetTop, offsetRight, offsetBottom);
    }

    /**
     * 设置条目的内嵌偏移长度
     * RecyclerView在measureChild()时调用此方法
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 设置 内嵌偏移长度,在条目left，top,right,bottom基础上,分别增加outRect对应的值(看源码)
        outRect.set(mDecorationHelper.getOffsetLeft(),
                mDecorationHelper.getOffsetTop(),
                mDecorationHelper.getOffsetRight(),
                mDecorationHelper.getOffsetBottom());
    }

    /**
     * 这个方法在条目绘制之前调用
     * 适合做分割线绘制
     * 绘制分割线:
     * ①遍历获取可见条目位置信息(RecyclerView的所有子view)
     * ②为可见条目绘制分割线
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        c.save();
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View view = parent.getChildAt(i);
            mDecorationHelper.drawLeftDecoration(c, view);
            mDecorationHelper.drawTopDecoration(c, view);
            mDecorationHelper.drawRightDecoration(c, view);
            mDecorationHelper.drawBottomDecoration(c, view);
        }
        c.restore();
    }
}


