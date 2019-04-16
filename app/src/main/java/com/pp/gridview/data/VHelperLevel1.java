package com.pp.gridview.data;

import android.support.annotation.DrawableRes;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.pp.gridview.adapter.BaseTitleMultiItemAdapter;

public class VHelperLevel1 implements MultiItemEntity {
    private String name;
    private @DrawableRes
    Integer iconId;

    public VHelperLevel1(String name, @DrawableRes Integer iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    @Override
    public int getItemType() {
        return BaseTitleMultiItemAdapter.TYPE_LEVEL_1;
    }
}
