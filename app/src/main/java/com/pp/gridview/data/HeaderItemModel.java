package com.pp.gridview.data;


import android.support.annotation.DrawableRes;

public class HeaderItemModel extends BaseHeaderItem {
    private String name;
    private @DrawableRes
    Integer iconId;

    public HeaderItemModel(String name, @DrawableRes Integer iconId) {
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
        return 0;
    }
}
