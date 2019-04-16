package com.pp.gridview.customview.decoration;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.view.View;

public class DecorationHelper {
    private int offsetLeft;
    private int offsetTop;
    private int offsetRight;
    private int offsetBottom;
    private final ColorDrawable mDivide;

    public DecorationHelper(@ColorInt int color, int offsetLeft, int offsetTop, int offsetRight, int offsetBottom) {
        mDivide = new ColorDrawable(color);
        this.offsetLeft = offsetLeft;
        this.offsetTop = offsetTop;
        this.offsetRight = offsetRight;
        this.offsetBottom = offsetBottom;
    }

    public int getOffsetLeft() {
        return offsetLeft;
    }

    public int getOffsetTop() {
        return offsetTop;
    }

    public int getOffsetRight() {
        return offsetRight;
    }

    public int getOffsetBottom() {
        return offsetBottom;
    }

    public void drawLeftDecoration(Canvas canvas, View view) {
        mDivide.setBounds(view.getLeft() - offsetLeft,
                view.getTop() - offsetTop,
                view.getLeft(),
                view.getBottom() + offsetBottom);
        mDivide.draw(canvas);
    }

    public void drawTopDecoration(Canvas c, View view) {
        mDivide.setBounds(view.getLeft() - offsetLeft,
                view.getTop() - offsetTop,
                view.getRight() + offsetRight,
                view.getTop());
        mDivide.draw(c);
    }

    public void drawRightDecoration(Canvas c, View view) {
        mDivide.setBounds(view.getRight(),
                view.getTop() - offsetTop,
                view.getRight() + offsetRight,
                view.getBottom() + offsetBottom);
        mDivide.draw(c);
    }

    public void drawBottomDecoration(Canvas c, View view) {
        mDivide.setBounds(view.getLeft() - offsetLeft,
                view.getBottom(),
                view.getRight() + offsetBottom,
                view.getBottom() + offsetBottom);
        mDivide.draw(c);
    }
}
