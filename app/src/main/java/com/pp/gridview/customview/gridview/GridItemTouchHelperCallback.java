package com.pp.gridview.customview.gridview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.List;

public class GridItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private int mToPos = -1;
    private RecyclerView.ViewHolder mTargetHolder;
    private boolean onMoving = false;

    public GridItemTouchHelperCallback(Context context) {

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlag = 0;  // 如果想支持滑动(删除)操作, swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return viewHolder != target;
    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return super.getMoveThreshold(viewHolder);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        mToPos = toPos;
        mTargetHolder = target;
        Log.e("TAG","************** onMoved: " + mToPos);
        if (null != onItemTouchHelperListener) {
            onItemTouchHelperListener.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        }
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.e("TAG","************** onSelectedChanged: " + actionState);
        if (null != onItemTouchHelperListener) {
            onItemTouchHelperListener.onSelectedChanged(viewHolder, actionState);
        }
        onMoving = false;
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) { //　开始拖动　item
        }
        super.onSelectedChanged(viewHolder, actionState);
    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (null != mTargetHolder) {
            Rect targetHect = new Rect();
            mTargetHolder.itemView.getHitRect(targetHect);
            Rect fromHect = new Rect();
            viewHolder.itemView.getHitRect(fromHect);
            if (!isCurrentlyActive
                    && targetHect.contains(fromHect.centerX(), fromHect.centerY())
                    && mTargetHolder != viewHolder) {
                Log.e("TAG","************** onChildDraw: " + actionState);
                Log.e("TAG","************** onChildDraw: " + viewHolder.getAdapterPosition());
                onItemTouchHelperListener.exChange(viewHolder, mTargetHolder, viewHolder.getAdapterPosition(), mToPos);
                mToPos = -1;
                mTargetHolder = null;
                onMoving = true;
            }
        }
        if (!onMoving) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }


    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (null != onItemTouchHelperListener) {
            onItemTouchHelperListener.clearView(recyclerView, viewHolder);
        }
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected, List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
        RecyclerView.ViewHolder winner = null;
        final int targetsSize = dropTargets.size();
        final Rect selectedRect = new Rect();
        selected.itemView.getHitRect(selectedRect);
        final int width = selectedRect.right - selectedRect.left;
        final int height = selectedRect.bottom - selectedRect.top;
        selectedRect.set(selectedRect.left + width * 1 / 4,
                selectedRect.top + height * 1 / 4,
                selectedRect.right - width * 1 / 4,
                selectedRect.bottom - height * 1 / 4);
        for (int i = 0; i < targetsSize; i++) {
            final RecyclerView.ViewHolder target = dropTargets.get(i);
            if (null == target) continue;
            final Rect targetRect = new Rect();
            target.itemView.getHitRect(targetRect);
            final boolean contains = selectedRect.contains(targetRect.centerX(), targetRect.centerY());
            if (contains) {
                winner = target;
                break;
            }
        }
        return winner;
    }

    private OnItemTouchHelperListener onItemTouchHelperListener;

    public void setOnItemTouchHelperListener(OnItemTouchHelperListener onItemTouchHelperListener) {
        this.onItemTouchHelperListener = onItemTouchHelperListener;
    }

    public interface OnItemTouchHelperListener {
        void exChange(RecyclerView.ViewHolder dragViewHolder, RecyclerView.ViewHolder targetViewHolder, int fromPos, int toPos);

        void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y);

        void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState);

        void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
    }
}



