package com.pp.gridview.customview.layoutmanager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.WindowManager;

import com.pp.gridview.adapter.BaseAdapter;
import com.pp.gridview.customview.deletebar.DeleteBarView;
import com.pp.gridview.customview.gridview.GridItemTouchHelperCallback;

public class GridItemTouchManager implements GridItemTouchHelperCallback.OnItemTouchHelperListener {


    private final BaseAdapter mAdapter;
    private final CoverFlowLayoutManager2 coverFlowLayoutManager2;
    private final Context mContext;
    private WindowManager.LayoutParams deletedBarParams;
    private DeleteBarView deleteBarView;
    private WindowManager windowManager;
    private boolean exChangeMode = true;

    public GridItemTouchManager(Context context, @NonNull CoverFlowLayoutManager2 coverFlowLayoutManager2, @NonNull BaseAdapter adapter) {
        this.mContext = context;
        this.mAdapter = adapter;
        this.coverFlowLayoutManager2 = coverFlowLayoutManager2;
        init();
    }

    private void init() {
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
        deleteBarView = new DeleteBarView(mContext);

        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setDeleteBarLayoutParams(int width, int height) {
        deleteBarView.setDeleteBarLayoutParams(width, height);
    }

    @Override
    public void exChange(RecyclerView.ViewHolder dragHolder, RecyclerView.ViewHolder targetViewHolder, int fromPos, int toPos) {
        if (null != dragHolder) {
            dragHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        if (exChangeMode && mAdapter != null)
            mAdapter.swapItem(fromPos, toPos);
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        if (y < -20) {
            deleteBarView.setSelected(true);
        } else {
            deleteBarView.setSelected(false);
        }
        if (!exChangeMode && mAdapter != null)
            mAdapter.itemMoved(fromPos, toPos);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(Color.RED);
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (coverFlowLayoutManager2 != null) {
                coverFlowLayoutManager2.setCanScroll(false);
            }
            if (null != deleteBarView.getParent()) {
                windowManager.removeViewImmediate(deleteBarView);
            } else {
                windowManager.addView(deleteBarView, deletedBarParams);
                windowManager.updateViewLayout(deleteBarView, deletedBarParams);
            }
        } else {
            if (coverFlowLayoutManager2 != null) {
                coverFlowLayoutManager2.setCanScroll(true);
            }
            if (null != deleteBarView.getParent()) {
                windowManager.removeViewImmediate(deleteBarView);
            }
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (null == viewHolder) return;
        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
    }
}

