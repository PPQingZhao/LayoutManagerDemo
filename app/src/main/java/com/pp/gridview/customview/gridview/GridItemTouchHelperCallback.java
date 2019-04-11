package com.pp.gridview.customview.gridview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.WindowManager;

import com.pp.gridview.customview.deletebar.DeleteBarView;

import java.util.List;

public class GridItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final WindowManager.LayoutParams deletedBarParams;
    private final DeleteBarView deleteBarView;
    private final WindowManager windowManager;
    private int mFromPos = -1;
    private int mToPos = -1;
    private RecyclerView.ViewHolder mTargetHolder;
    private boolean onMoving = false;

    public GridItemTouchHelperCallback(Context context) {
        deletedBarParams = new WindowManager.LayoutParams();
        deletedBarParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        deletedBarParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        deletedBarParams.gravity = Gravity.TOP | Gravity.LEFT;
        deletedBarParams.format = PixelFormat.RGBA_8888;
        deletedBarParams.windowAnimations = 0;
        deletedBarParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        deleteBarView = new DeleteBarView(context);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setDeleteBarLayoutParams(int width, int height) {
        deleteBarView.setDeleteBarLayoutParams(width, height);
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
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        if (y < -20) {
            deleteBarView.setSelected(true);
        } else {
            deleteBarView.setSelected(false);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (null != onItemTouchHelperListener) {
            onItemTouchHelperListener.onSelectedChanged(viewHolder, actionState);
        }
        onMoving = false;
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) { //　开始拖动　item
            mFromPos = viewHolder.getAdapterPosition();
        }
        super.onSelectedChanged(viewHolder, actionState);
        if (null != deleteBarView.getParent()) {
            windowManager.removeViewImmediate(deleteBarView);
        } else {
            windowManager.addView(deleteBarView, deletedBarParams);
            windowManager.updateViewLayout(deleteBarView, deletedBarParams);
        }
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
                onItemTouchHelperListener.move(viewHolder, mFromPos, mToPos);
                mFromPos = -1;
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
        void move(RecyclerView.ViewHolder dragViewHolder, int fromPos, int toPos);

        void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState);

        void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
    }
}



