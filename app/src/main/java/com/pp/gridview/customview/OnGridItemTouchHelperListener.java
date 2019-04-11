package com.pp.gridview.customview;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.pp.gridview.adapter.BaseAdapter;
import com.pp.gridview.customview.gridview.GridItemTouchHelperCallback;

public class OnGridItemTouchHelperListener implements GridItemTouchHelperCallback.OnItemTouchHelperListener {


    private final BaseAdapter mAdapter;
    private final CoverFlowLayoutManager2 coverFlowLayoutManager2;

    public OnGridItemTouchHelperListener(@NonNull CoverFlowLayoutManager2 coverFlowLayoutManager2, @NonNull BaseAdapter adapter) {
        this.mAdapter = adapter;
        this.coverFlowLayoutManager2 = coverFlowLayoutManager2;
    }

    @Override
    public void move(RecyclerView.ViewHolder dragHolder, int fromPos, int toPos) {
        if (null != dragHolder) {
            dragHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        if (mAdapter != null)
            mAdapter.swapItem(fromPos, toPos);
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
        } else {
            if (coverFlowLayoutManager2 != null) {
                coverFlowLayoutManager2.setCanScroll(true);
            }
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (null == viewHolder) return;
        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
    }
}

