package com.pp.gridview.customview.layoutmanager;

import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

public class CoverFlowLayoutManager2 extends RecyclerView.LayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider {

    public final static int VERTICAL = 0;
    public final static int HORIZONTAL = 1;

    @IntDef({VERTICAL, HORIZONTAL})
    public @interface OrientationMode {
    }

    private static final int NONE_DIRECTION = 0;
    private static final int POSITIVE_DIRECTION = 1;
    private static final int NEGATIVE_DIRECTION = -1;
    private int oldState = RecyclerView.SCROLL_STATE_IDLE;
    private RecyclerView recyclerView;
    /*手指松开后是否进行粘性滚动*/
    private boolean finishScroll;
    private boolean scrollPagerModel = true;


    /**
     * 标记滑动方向
     * ①竖直滑动:
     * POSITIVE_DIRECTION　　item向上滑动
     * NEGATIVE_DIRECTION　　item向下滑动
     * ②横向滑动
     * POSITIVE_DIRECTION　　item向左滑动
     * NEGATIVE_DIRECTION　　item向右滑动
     */
    private int slidingDirection = NONE_DIRECTION;
    private int orientation = VERTICAL;
    /*记录item对应的矩形位置*/
    private final SparseArray<Rect> allItemframs = new SparseArray<>();
    /*记录　item是否已经添加到recyclerview中*/
    private final HashMap<Integer, Boolean> itemAttachedState = new HashMap<>();
    //列
    private int columCount = 2;
    //行
    private int rowCount = 2;

    private int totalHeight;
    private int totalWidth;
    /*竖直方向偏移量*/
    private int verticalScrollOffset;
    /*水平方向偏移量*/
    private int horizontalScrollOffset;
    /*是否可以滑动*/
    private boolean canScroll = true;

    public CoverFlowLayoutManager2(int columCount, int rowCount) {
        this(columCount, rowCount, VERTICAL);
    }

    public CoverFlowLayoutManager2(int columCount, int rowCount, @OrientationMode int orientation) {
        this.columCount = columCount;
        this.rowCount = rowCount;
        this.orientation = orientation;
    }

    /**
     * 　重写这个方法可以实现smoothScrollToPosition滑动
     *
     * @param targetPosition
     * @return
     */
    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        Log.e("TAG", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@computeScrollVectorForPosition");
        if (getChildCount() == 0) {
            return null;
        }
        final int firstChildPos = getPosition(getChildAt(0));
        final int direction = targetPosition < firstChildPos ? -1 : 1;
        if (orientation == HORIZONTAL) {
            return new PointF(direction, 0);
        } else {
            return new PointF(0, direction);
        }
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.e("TAG", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  onLayoutChildren  start");
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
//        recycler.setViewCacheSize(orientation == VERTICAL ? columCount : rowCount);
        itemAttachedState.clear();
        totalHeight = 0;
        totalWidth = 0;
         /* final ArrayList<ViewHolder> mAttachedScrap = new ArrayList<>(); 一级缓存   (用于重新布局使用)
        ArrayList<ViewHolder> mChangedScrap = null; 这里存的是不可见的item(刚滑出屏幕),再次取出时item内容不变

        final ArrayList<ViewHolder> mCachedViews = new ArrayList<ViewHolder>();　二级缓存　(用于重复上下滑动使用)
        private RecycledViewPool mRecyclerPool;　　三级缓存
        private ViewCacheExtension mViewCacheExtension;　　四级缓存

        ①　mAttachedScrap其实跟item的复用没有关系，
            它是recyclerview在layout它的child的时候经历了先移除再添加的过程，
            再添加就是从这个mAttachedScrap去取，白话文就是屏幕内item的复用
        ②　mCacheViews只存2个viewHolder
        ③　mViewCacheExtension留给开发者扩展的，可以先忽略
        ④　mRecyclerPool缓存5个viewholder
        */

        /*
                重新布局,先deatch所有item,将当前getchildcount数量的子View放入到scrap缓存池去
            (这里缓存的item再次取出时,item的内容没有发生改变)
        */
        detachAndScrapAttachedViews(recycler);
        int recycleViewWidth = getWidth();
        int recyclerViewHeight = getHeight();
        Rect recyclerViewFrame = getRecyclerViewFrame();
        for (int i = 0; i < getItemCount(); i++) {
            //查找顺序 :
            // mAttachedScrap(根据position查找)
            // ->mCachedViews(根据position查找) --只有position相同的才复用,所以这个只有在重复上下滑动时才起作用
            // -> mAdapter.hasStableIds()
            // ->RecycledViewPool(根据viewtype查找,adapter里要重新再onBindViewHolder)
            // ->还没有找到就调用到adapter的CreateViewHolder创建

            int decoratedMeasuredWidth = /*getDecoratedMeasuredWidth(child)*/recycleViewWidth / columCount;
            int decoratedMeasuredHeight = /*getDecoratedMeasuredHeight(child)*/recyclerViewHeight / rowCount;
            int left = getLeftValue(i, recycleViewWidth, decoratedMeasuredWidth);
            int top = getTopValue(i, recyclerViewHeight, decoratedMeasuredHeight);
            int right = getRightValue(i, recycleViewWidth, decoratedMeasuredWidth);
            int bottom = getBottomValue(i, recyclerViewHeight, decoratedMeasuredHeight);
            Rect rect = new Rect(left, top, right, bottom);
            allItemframs.put(i, rect);
            if (Rect.intersects(recyclerViewFrame, allItemframs.get(i))) {
//                Log.e("TAG", i + "   ************** rect: " + rect);
                //添加显示item
                if (null == itemAttachedState.get(i) || !itemAttachedState.get(i)) {
                    View child = recycler.getViewForPosition(i);
                    int measureSpecWidth = View.MeasureSpec.makeMeasureSpec(decoratedMeasuredWidth, View.MeasureSpec.EXACTLY);
                    int measureSpecHeight = View.MeasureSpec.makeMeasureSpec(decoratedMeasuredHeight, View.MeasureSpec.EXACTLY);
                    child.measure(measureSpecWidth, measureSpecHeight);
                    addView(child);
                    layoutItem(child, allItemframs.get(i));
                    itemAttachedState.put(i, true);
                }
            }
            //计算摆放所有item所需要的长度和高度
            int columIndex = calucColumInPager(i);
            int rowIndex = calucRowInPager(i);
            if (columIndex == 0 && rowIndex == 0) {
                if (orientation == HORIZONTAL) {
                    totalHeight = recyclerViewHeight;
                    totalWidth += recycleViewWidth;
                } else {
                    totalHeight += recyclerViewHeight;
                    totalWidth = recycleViewWidth;
                }
            }
        }
        if (null != onPagerChangeListener) {
            onPagerChangeListener.onPageSelected(getCurrPagerIndex());
        }
        Log.e("TAG", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  onLayoutChildren  end");
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        CoverFlowSmoothScroller smoothScroller = new CoverFlowSmoothScroller(recyclerView.getContext(), this);
        //计算position在页面的索引(第几个位置)
        int itemIndexInPager = calucItemIndexInPager(position);
        if (orientation == VERTICAL) {
            smoothScroller.setTargetPosition(position - itemIndexInPager);
        } else {
            smoothScroller.setTargetPosition(position - itemIndexInPager + (columCount - 1));
        }
        startSmoothScroll(smoothScroller);
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        if (position < 0 || position >= getItemCount()) {
            Log.e("TAG", "The position passed to the execution method is invalid.");
            return;
        }
        // 计算position对应的item所在页的索引
        int pagerIndex = calcuPagerIndex(position);
        //计算偏移量
        if (orientation == VERTICAL) {
            verticalScrollOffset = pagerIndex * getHeight();
        } else {
            horizontalScrollOffset = pagerIndex * getWidth();
        }
        //重新布局
        requestLayout();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //水平方向滑动
        int scrollＸ = dx;
        if (horizontalScrollOffset + dx < 0) { //抵达左边界
            scrollＸ = 0 - horizontalScrollOffset;
        } else if (horizontalScrollOffset + dx > (totalWidth - getWidth())) { //抵达右边界
            scrollＸ = (totalWidth - getWidth()) - horizontalScrollOffset;
        }
        if (finishScroll) { //手指抬起，页面进行粘性滑动
            slidingDirection = slidingDirection;
        } else if (scrollＸ > 0) { //向左边滑动
            slidingDirection = POSITIVE_DIRECTION;
        } else if (scrollＸ < 0) { //向右边滑动
            slidingDirection = NEGATIVE_DIRECTION;
        }
        //竖直方向偏移量
        horizontalScrollOffset += scrollＸ;
        //开始移动
        offsetChildrenHorizontal(0 - scrollＸ);
        recyclerViewFillView(recycler);
//        recyclerViewFillView2(recycler);
        return scrollＸ;
    }

    @Override
    public synchronized int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //竖直方向滑动
        int scrollY = dy;
        if (verticalScrollOffset + dy < 0) { //抵达上边界
            scrollY = 0 - verticalScrollOffset;
        } else if (verticalScrollOffset + dy > (totalHeight - getHeight())) { //抵达下边界
            scrollY = (totalHeight - getHeight()) - verticalScrollOffset;
        }
        if (finishScroll) {
            slidingDirection = slidingDirection;
        } else if (scrollY > 0) { //向上滑动
            slidingDirection = POSITIVE_DIRECTION;
        } else if (scrollY < 0) { //向下滑动
            slidingDirection = NEGATIVE_DIRECTION;
        }
        //竖直方向偏移量
        verticalScrollOffset += scrollY;
        //开始移动
        offsetChildrenVertical(0 - scrollY);
        //回收item
        recyclerViewFillView(recycler);
//        recyclerViewFillView2(recycler);
        return scrollY;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                //停止滚动
                canScroll = true;
                finishScroll = scrollPagerModel ? fixOffsetWhenFinishScroll() : false;
                if (!finishScroll) {
                    if (null != onPagerChangeListener) {
                        onPagerChangeListener.onPageSelected(getCurrPagerIndex());
                    }
                }
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                //开始拖拽滚动
                canScroll = true;
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                //开始动画滚动　(惯性滚动)
                canScroll = scrollPagerModel ? oldState != RecyclerView.SCROLL_STATE_DRAGGING : true;
                break;
        }
        oldState = state;
    }

    private boolean fixOffsetWhenFinishScroll() {
        if (null == recyclerView) return false;
        //计算最后一页的滚动偏移量
        int pagerHorizontalOffset = horizontalScrollOffset % getWidth();
        int pagerVerticalOffset = verticalScrollOffset % getHeight();
        //计算当前偏移量代表的页的索引
        if (pagerHorizontalOffset > 0 || pagerVerticalOffset > 0) {
            int dx = 0;
            int dy = 0;
            if (slidingDirection == POSITIVE_DIRECTION) {
                dx = pagerHorizontalOffset == 0 ?
                        0 : (pagerHorizontalOffset < getWidth() / 4 ? 0 - pagerHorizontalOffset /*不能到达目标页面*/ : getWidth() - pagerHorizontalOffset);
                dy = pagerVerticalOffset == 0 ?
                        0 : (pagerVerticalOffset < getHeight() / 4 ? 0 - pagerVerticalOffset /*不能到达目标页面*/ : getHeight() - pagerVerticalOffset);
                recyclerView.smoothScrollBy(dx, dy);
            } else if (slidingDirection == NEGATIVE_DIRECTION) {
                dx = pagerHorizontalOffset == 0 ?
                        0 : (pagerHorizontalOffset < getWidth() * 3 / 4 ? 0 - pagerHorizontalOffset : getWidth() - pagerHorizontalOffset /*不能到达目标页面*/);
                dy = pagerVerticalOffset == 0 ?
                        0 : (pagerVerticalOffset < getHeight() * 3 / 4 ? 0 - pagerVerticalOffset : getHeight() - pagerVerticalOffset /*不能到达目标页面*/);
                recyclerView.smoothScrollBy(dx, dy);
            } else {
            }
            return true;
        }
        return false;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    /**
     * 1.添加可见范围的item并确定item位置
     * 2.回收不可见的item
     *
     * @param recycler
     */
    private void recyclerViewFillView(RecyclerView.Recycler recycler) {
        int recycleViewWidth = getWidth();
        int recyclerViewHeight = getHeight();
        //recyclerView的矩形位置
        Rect recyclerViewFrame = getRecyclerViewFrame();
        //将滑出屏幕的view进行回收
        for (int i = 0; i < getChildCount(); i++) {
            //①　获取　i索引的child ，这里要注意并发问题,在该for循环中执行了remove(view)方法之后,i要减１
            View childView = getChildAt(i);
            int position = getPosition(childView);
            if (!Rect.intersects(recyclerViewFrame, allItemframs.get(position))) {
                //item 不可见,recyclerview移除当前childview，并将之放入到recycler缓存池
                removeAndRecycleView(childView, recycler);

                itemAttachedState.put(position, false);
                i--;
            } else {  //Item还在显示区域内，更新滑动后Item的位置
                itemAttachedState.put(position, true);
            }
        }

//        可见区域显示在屏幕上的子view
        for (int j = 0; j < getItemCount(); j++) {
            if (Rect.intersects(recyclerViewFrame, allItemframs.get(j))) {
                if (null == itemAttachedState.get(j) || !itemAttachedState.get(j)) {
                    View scrap = recycler.getViewForPosition(j); //这个方法耗时(四级缓存查找),不必要不要执行(不要放在if判断之前)
                    int measureSpecWidth = View.MeasureSpec.makeMeasureSpec(recycleViewWidth / columCount, View.MeasureSpec.EXACTLY);
                    int measureSpecHeight = View.MeasureSpec.makeMeasureSpec(recyclerViewHeight / rowCount, View.MeasureSpec.EXACTLY);
                    scrap.measure(measureSpecWidth, measureSpecHeight);
                    if (slidingDirection == NEGATIVE_DIRECTION) { //向下滑动
                        addView(scrap, j % columCount);
                    } else {
                        addView(scrap);
                    }
                    layoutItem(scrap, allItemframs.get(j));
                }
                itemAttachedState.put(j, true);
            } else {
                itemAttachedState.put(j, false);
            }
        }
    }

    private void recyclerViewFillView2(RecyclerView.Recycler recycler) {
        int recycleViewWidth = getWidth();
        int recyclerViewHeight = getHeight();
        //recyclerView的矩形位置
        Rect recyclerViewFrame = getRecyclerViewFrame();
        //将滑出屏幕的view进行回收
        for (int i = 0; i < getChildCount(); i++) {
            //①　获取　i索引的child ，这里要注意并发问题,在该for循环中执行了remove(view)方法之后,i要减１
            View childView = getChildAt(i);
            int position = getPosition(childView);
            if (!Rect.intersects(recyclerViewFrame, allItemframs.get(position))) {
                //item 不可见,recyclerview移除当前childview，并将之放入到recycler缓存池
                removeAndRecycleView(childView, recycler);
                i--;
            }
        }
        detachAndScrapAttachedViews(recycler);
//        可见区域出现在屏幕上的子view
        for (int j = 0; j < getItemCount(); j++) {
            if (Rect.intersects(recyclerViewFrame, allItemframs.get(j))) {
                View scrap = recycler.getViewForPosition(j);
                int measureSpecWidth = View.MeasureSpec.makeMeasureSpec(recycleViewWidth / columCount, View.MeasureSpec.EXACTLY);
                int measureSpecHeight = View.MeasureSpec.makeMeasureSpec(recyclerViewHeight / rowCount, View.MeasureSpec.EXACTLY);
                scrap.measure(measureSpecWidth, measureSpecHeight);
                if (slidingDirection == NEGATIVE_DIRECTION) { //向下滑动
                    addView(scrap, j % columCount);
                } else {
                    addView(scrap);
                }
                layoutItem(scrap, allItemframs.get(j));
            }
        }
    }

    /**
     * 布局item
     *
     * @param child
     * @param frame
     */
    private void layoutItem(View child, Rect frame) {
        layoutDecorated(child,
                frame.left - horizontalScrollOffset,
                frame.top - verticalScrollOffset,
                frame.right - horizontalScrollOffset,
                frame.bottom - verticalScrollOffset);
    }

    /**
     * 获取recyclerview相对与条目的矩形位置(发生滑动偏移)
     *
     * @return
     */
    private Rect getRecyclerViewFrame() {
        int recycleViewWidth = getWidth();
        int recyclerViewHeight = getHeight();

        Rect recyclerViewFrame = new Rect(0 + horizontalScrollOffset,
                0 + verticalScrollOffset,
                recycleViewWidth + horizontalScrollOffset,
                recyclerViewHeight + verticalScrollOffset);
        return recyclerViewFrame;
    }

    @Override
    public boolean canScrollVertically() {
        return orientation == VERTICAL && canScroll;
    }

    @Override
    public boolean canScrollHorizontally() {
        return orientation == HORIZONTAL && canScroll;
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        super.onAdapterChanged(oldAdapter, newAdapter);
        allItemframs.clear();
        itemAttachedState.clear();
        totalWidth = 0;
        totalHeight = 0;
        verticalScrollOffset = 0;
        slidingDirection = NONE_DIRECTION;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        recyclerView = view;
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        recyclerView = null;
    }

    public int getPageCount() {
        return orientation == HORIZONTAL ? totalWidth / getWidth() : totalHeight / getHeight();
    }

    private int getCurrPagerIndex() {
        return orientation == HORIZONTAL ? horizontalScrollOffset / getWidth() : verticalScrollOffset / getHeight();
    }

    /**
     * 计算item 的下边缘
     *
     * @param itemPosition
     * @param recyclerViewHeight
     * @param childHeight
     * @return
     */
    private int getBottomValue(int itemPosition, int recyclerViewHeight, int childHeight) {
        return getTopValue(itemPosition, recyclerViewHeight, childHeight) + childHeight;
    }

    /**
     * 计算item 的右边缘
     *
     * @param itemPosition
     * @param recycleViewWidth
     * @param childWidth
     * @return
     */
    private int getRightValue(int itemPosition, int recycleViewWidth, int childWidth) {
        return getLeftValue(itemPosition, recycleViewWidth, childWidth) + childWidth;
    }

    /**
     * 计算item 的上边缘
     *
     * @param itemPosition
     * @param recyclerViewHeight
     * @param childHeight
     * @return
     */
    private int getTopValue(int itemPosition, int recyclerViewHeight, int childHeight) {
        int rowIndex = calucRowInPager(itemPosition); //item　在当前页面的行
        if (orientation == HORIZONTAL) {
            return rowIndex * childHeight;
        } else {
            int pagerIndex = calcuPagerIndex(itemPosition);   //item 所在页面的索引
            return rowIndex * childHeight + pagerIndex * recyclerViewHeight;
        }
    }

    /**
     * 计算item 的左边缘
     *
     * @param itemPosition
     * @param recycleViewWidth
     * @param childWidth
     * @return
     */
    private int getLeftValue(int itemPosition, int recycleViewWidth, int childWidth) {
        int columIndex = calucColumInPager(itemPosition); //item　在当前页面的列
        if (orientation == HORIZONTAL) {
            int pagerIndex = calcuPagerIndex(itemPosition);   //item 所在页面的索引
            return childWidth * columIndex + pagerIndex * recycleViewWidth;
        } else {
            return childWidth * columIndex;
        }
    }

    /**
     * 计算index对应的item在当前页面的行(从0开始)
     *
     * @param index
     * @return
     */
    public int calucRowInPager(int index) {
        return index % (columCount * rowCount) / columCount;
    }

    /**
     * 计算index对应的item在当前页面的列(从0开始)
     *
     * @param index
     * @return
     */
    public int calucColumInPager(int index) {
        return index % (columCount * rowCount) % columCount;
    }

    /**
     * 计算position对应的item所在页的索引(从0开始)
     *
     * @param position
     * @return
     */
    public int calcuPagerIndex(int position) {
        return position / (columCount * rowCount);
    }


    /**
     * 计算position对应条目在页面的索引(第几个位置)
     *
     * @param position
     * @return
     */
    private int calucItemIndexInPager(int position) {
        //计算position在页面的列
        int columInPager = calucColumInPager(position);
        //计算position在页面的行
        int rowInPager = calucRowInPager(position);
        return rowInPager * columCount + columInPager;
    }

    private OnPagerChangeListener onPagerChangeListener;

    public void setOnPagerChangeListener(OnPagerChangeListener onPagerChangeListener) {
        this.onPagerChangeListener = onPagerChangeListener;
    }

    public interface OnPagerChangeListener {
        void onPageSelected(int position);
    }
}
