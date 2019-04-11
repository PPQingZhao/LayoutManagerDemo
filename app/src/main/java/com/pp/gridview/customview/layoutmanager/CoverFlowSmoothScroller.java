package com.pp.gridview.customview.layoutmanager;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSmoothScroller;
import android.util.DisplayMetrics;

public class CoverFlowSmoothScroller extends LinearSmoothScroller {
    private final ScrollVectorProvider mScrollVectorProvider;

    public CoverFlowSmoothScroller(Context context, ScrollVectorProvider scrollVectorProvider) {
        super(context);
        mScrollVectorProvider = scrollVectorProvider;
    }

    @Nullable
    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        return mScrollVectorProvider.computeScrollVectorForPosition(targetPosition);
    }

    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return 0.2f;
    }

    //这个控制(竖直方向精确)滑动结束 item是处于start位置或者end位置

    /**
     * 这个控制滑动结束 item是处于start位置或者end位置
     * ①　竖直方向滑动 SNAP_TO_START
     * ②　水平方向滑动　不准确
     *
     * @return
     */
    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;
    }
}
