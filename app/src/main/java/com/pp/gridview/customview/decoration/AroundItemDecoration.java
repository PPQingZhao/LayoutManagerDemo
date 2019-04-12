package com.pp.gridview.customview.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class AroundItemDecoration extends RecyclerView.ItemDecoration {
    private int offsetLeft;
    private int offsetTop;
    private int offsetRight;
    private int offsetBottom;
    private final Drawable mDivide;

    public AroundItemDecoration(@ColorInt int color,
                                int offsetLeft,
                                int offsetTop,
                                int offsetRight,
                                int offsetBottom) {
        mDivide = new ColorDrawable(color);
        this.offsetLeft = offsetLeft;
        this.offsetTop = offsetTop;
        this.offsetRight = offsetRight;
        this.offsetBottom = offsetBottom;
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
        outRect.set(offsetLeft, offsetTop, offsetRight, offsetBottom);
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
            drawLeftDecoration(c, view);
            drawTopDecoration(c, view);
            drawRightDecoration(c, view);
            drawBottomDecoration(c, view);
        }
        c.restore();
    }

    private void drawLeftDecoration(Canvas c, View view) {
        mDivide.setBounds(view.getLeft() - offsetLeft,
                view.getTop() - offsetTop,
                view.getRight(),
                view.getBottom() + offsetBottom);
        mDivide.draw(c);
    }

    /**
     * 这个方法在条目绘制之后调用
     * 设置做粘性标题绘制
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }


    private void drawTopDecoration(Canvas c, View view) {
        mDivide.setBounds(view.getLeft() - offsetLeft,
                view.getTop() - offsetTop,
                view.getRight() + offsetRight,
                view.getBottom());
        mDivide.draw(c);
    }

    private void drawRightDecoration(Canvas c, View view) {
        mDivide.setBounds(view.getLeft(),
                view.getTop() - offsetTop,
                view.getRight() + offsetRight,
                view.getBottom() + offsetBottom);
        mDivide.draw(c);
    }

    private void drawBottomDecoration(Canvas c, View view) {
        mDivide.setBounds(view.getLeft() - offsetLeft,
                view.getTop(),
                view.getRight() + offsetBottom,
                view.getBottom() + offsetBottom);
        mDivide.draw(c);
    }
}

