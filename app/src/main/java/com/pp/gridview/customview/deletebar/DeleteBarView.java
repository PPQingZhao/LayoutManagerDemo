package com.pp.gridview.customview.deletebar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.pp.gridview.R;

public class DeleteBarView extends FrameLayout {

    private View iv_delete;

    public DeleteBarView(@NonNull Context context) {
        this(context, null);
    }

    public DeleteBarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeleteBarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.view_deletebar, this);
        iv_delete = findViewById(R.id.iv_delete);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        iv_delete.setSelected(selected);
    }

    public void setDeleteBarLayoutParams(int width, int height) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) iv_delete.getLayoutParams();
        if (null == layoutParams) {
            layoutParams = new ConstraintLayout.LayoutParams(width, height);
        } else {
            layoutParams.width = width;
            layoutParams.height = height;
        }
        iv_delete.setLayoutParams(layoutParams);
    }
}
