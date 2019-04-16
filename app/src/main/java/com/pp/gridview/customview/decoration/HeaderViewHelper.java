package com.pp.gridview.customview.decoration;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.pp.gridview.adapter.BaseHeaderAdapter;

class HeaderViewHelper {
    private BaseHeaderAdapter adapter;

    public void setAdapter(BaseHeaderAdapter adapter) {
        this.adapter = adapter;
    }

    View getHeaderView(@NonNull ViewGroup parent, final int position) {
        BaseHeaderAdapter.BaseHeaderViewHolder baseHeaderViewHolder = adapter.onCreateHeaderViewHolder(parent, 0);
        adapter.onBindHeaderViewHolder(baseHeaderViewHolder, position);
        return baseHeaderViewHolder.header;
    }
}
