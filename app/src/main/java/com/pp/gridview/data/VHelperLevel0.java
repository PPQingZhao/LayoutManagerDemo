package com.pp.gridview.data;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.pp.gridview.adapter.BaseTitleMultiItemAdapter;

public class VHelperLevel0 extends AbstractExpandableItem<VHelperLevel1> implements MultiItemEntity {
    private String levelName;

    public VHelperLevel0(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return BaseTitleMultiItemAdapter.TYPE_LEVEL_0;
    }
}
