package com.pp.gridview.data;


import android.support.annotation.DrawableRes;

public class ItemModel {
    private String name;
    private @DrawableRes
    Integer iconId;

    public ItemModel(String name, @DrawableRes Integer iconId) {
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
}
