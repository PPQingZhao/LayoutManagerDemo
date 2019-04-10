package com.pp.gridview.customview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SingleGridView extends ViewGroup {

    public static final int Model_1_1_1 = 0;
    public static final int Model_2_2_4 = 1;
    public static final int Model_3_3_6 = 2;
    public static final int Model_3_3_9 = 3;
    public static final int Model_4_4_8 = 4;
    public static final int Model_4_4_16 = 5;
    private int downX;
    private GestureDetector mGestureDetector;

    @IntDef({Model_1_1_1,
            Model_2_2_4,
            Model_3_3_6, Model_3_3_9,
            Model_4_4_8, Model_4_4_16})
    public @interface LayoutModel {
    }

    @LayoutModel
    private int mLayoutModel; //默认 Model_1_1_1

    public SingleGridView(Context context) {
        this(context, null);
    }

    public SingleGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLayoutModel = Model_1_1_1;
        mGestureDetector = new GestureDetector(getContext(), onGestureListener);
    }

    private GridMode obtainGridModel() {
        GridMode gridMode = null;
        switch (mLayoutModel) {
            case Model_1_1_1:
                gridMode = new GridMode(1, 1, 1);
                break;
            case Model_2_2_4:
                gridMode = new GridMode(2, 2, 4);
                break;
            case Model_3_3_6:
                gridMode = new GridMode(3, 3, 6);
                break;
            case Model_3_3_9:
                gridMode = new GridMode(3, 3, 9);
                break;
            case Model_4_4_8:
                gridMode = new GridMode(4, 4, 8);
                break;
            case Model_4_4_16:
                gridMode = new GridMode(4, 4, 16);
                break;
        }
        return gridMode;
    }

    public int getLayoutModel() {
        return mLayoutModel;
    }

    public void setLayoutModel(@LayoutModel int mLayoutModel) {
        this.mLayoutModel = mLayoutModel;
        obtainGridModel();
    }

    public int getShowCount() {
        GridMode gridMode = obtainGridModel();
        return gridMode.getShowCount();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        GridMode gridMode = obtainGridModel();
        int rowCount = gridMode.getRowCount();
        int columCount = gridMode.getColumCount();
        int showCount = gridMode.getShowCount();
        int maxChildCount = rowCount * columCount;
        int childWidthMeasureSpec = 0;
        int childHeightMeasureSpec = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (null == childAt) continue;
            if (maxChildCount == showCount) { //平均分布
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width / columCount, MeasureSpec.EXACTLY);
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height / rowCount, MeasureSpec.EXACTLY);
            } else {
                if (i == 0) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width / columCount * (columCount - 1), MeasureSpec.EXACTLY);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height / rowCount * (rowCount - 1), MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width / columCount, MeasureSpec.EXACTLY);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height / rowCount, MeasureSpec.EXACTLY);
                }
            }
            measureChild(childAt, childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        GridMode gridMode = obtainGridModel();
        int rowCount = gridMode.getRowCount();
        int columCount = gridMode.getColumCount();
        int showCount = gridMode.getShowCount();
        int maxChildCount = rowCount * columCount;
        int left = 0, top = 0, right, bottom = 0;
        int averageWidth = getWidth() / columCount;
        int averageHeight = getHeight() / rowCount;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (maxChildCount == showCount) { //平均模式
                int columIndex = i % columCount; //childAt所在列
                int rowIndex = i / rowCount;     //childAt所在行
                left = columIndex * averageWidth;
                top = rowIndex * averageHeight;
                right = left + averageWidth;
                bottom = top + averageHeight;
            } else {
                if (i == 0) {
                    left = 0;
                    top = 0;
                    right = averageWidth * (columCount - 1);
                    bottom = averageHeight * (rowCount - 1);
                } else {
                    if (i < (columCount - 1)) {
                        int columIndex = (columCount * i - 1) % columCount;
                        int rowIndex = (columCount * i - 1) / rowCount;
                        left = columIndex * averageWidth;
                        top = rowIndex * averageHeight;
                        right = left + averageWidth;
                        bottom = top + averageHeight;
                    } else {
                        int columIndex = (i + ((columCount - 1) * (rowCount - 1) - 1)) % columCount;
                        int rowIndex = (i + ((columCount - 1) * (rowCount - 1) - 1)) / rowCount;
                        left = columIndex * averageWidth;
                        top = rowIndex * averageHeight;
                        right = left + averageWidth;
                        bottom = top + averageHeight;
                    }
                }
            }
            childAt.layout(left, top, right, bottom);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("TAG", "*****************onTouchEvent MotionEvent.ACTION_DOWN ");
                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                Log.e("TAG", "*****************onTouchEvent MotionEvent.ACTION_HOVER_MOVE ");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("TAG", "*****************onTouchEvent MotionEvent.ACTION_UP ");
                break;
        }
        return true;
    }

    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.e("TAG", "*****************onDown ");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.e("TAG", "*****************onShowPress ");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e("TAG", "*****************onSingleTapUp ");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e("TAG", "*****************onScroll ");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.e("TAG", "*****************onLongPress ");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e("TAG", "*****************onFling ");
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e("TAG", "*****************onDoubleTap ");
            return super.onDoubleTap(e);
        }
    };

    private View getChildByPoints(int eventX, int eventY) {
        Rect rect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            //获取对应矩形坐标
            childAt.getHitRect(rect);
            if (childAt.getVisibility() == VISIBLE
                    && rect.contains(eventX, eventY)) { // 坐标点在矩形内
                return childAt;
            }
        }
        return null;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
