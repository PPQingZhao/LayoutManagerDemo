package com.pp.gridview.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.pp.gridview.R;
import com.pp.gridview.data.VHelperLevel0;
import com.pp.gridview.data.VHelperLevel1;

import java.util.List;

public class VHBaseAdapter<T extends MultiItemEntity> extends BaseTitleMultiItemAdapter<T> {

    public VHBaseAdapter(List<T> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.layout_level0);
        addItemType(TYPE_LEVEL_1, R.layout.layout_level1);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        switch (item.getItemType()) {
            case TYPE_LEVEL_0:
                VHelperLevel0 vHelperLevel0 = (VHelperLevel0) item;
                helper.setText(R.id.header＿tv, vHelperLevel0.getLevelName());
                break;
            case TYPE_LEVEL_1:
                VHelperLevel1 vHelperLevel1 = (VHelperLevel1) item;
                helper.setText(R.id.level1_tv, vHelperLevel1.getName());
                helper.setImageResource(R.id.level1_iv, vHelperLevel1.getIconId());
                break;
        }
    }
}
