package com.pp.gridview.data;

public abstract class BaseHeaderItem {
    private boolean containTitle;

    public boolean isContainTitle() {
        return containTitle;
    }

    public void setContainTitle(boolean containTitle) {
        this.containTitle = containTitle;
    }

    public abstract int getItemType();
}
