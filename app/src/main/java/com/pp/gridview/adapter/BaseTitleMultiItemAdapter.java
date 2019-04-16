package com.pp.gridview.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public abstract class BaseTitleMultiItemAdapter<T extends MultiItemEntity> extends BaseMultiItemQuickAdapter<T, BaseViewHolder> {

    /*用于区分条目类型*/
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    /*用于存储悬浮标题栏的viewholder*/
    private final SparseArray<BaseViewHolder> titleViewHolderArr = new SparseArray<>();
    /*用于存储布局类型对应的布局id*/
    private final SparseIntArray layoutResIdArr = new SparseIntArray();

    public BaseTitleMultiItemAdapter(List<T> data) {
        super(data);
    }

    @Override
    protected void addItemType(int type, int layoutResId) {
        super.addItemType(type, layoutResId);
        layoutResIdArr.put(type, layoutResId);
    }

    /**
     * 主要用于创建悬浮标题栏
     *
     * @param parent
     * @param position
     * @param viewType
     * @return
     */
    public View creatTitleView(RecyclerView parent, int position, int viewType) {
        BaseViewHolder baseViewHolder = createDefViewHolder(parent, viewType);
        convert(baseViewHolder, getItem(position));
        return baseViewHolder.itemView;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("TAG","*******************onCreateViewHolder ");
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Log.e("TAG","**************** onBindViewHolder  "+ position);
    }

    /**
     * 根据传入的viewType创建对应的viewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected BaseViewHolder createDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = titleViewHolderArr.get(viewType);
        if (null == baseViewHolder) {
            Log.e("TAG","**************** 222222createDefViewHolder ");
            baseViewHolder = onCreateViewHolder(parent, viewType);
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);
            baseViewHolder.itemView.measure(widthMeasureSpec, heightMeasureSpec);
            //只有执行　layout()方法之后,view才有宽高信息
            baseViewHolder.itemView.layout(0, 0, baseViewHolder.itemView.getMeasuredWidth(), baseViewHolder.itemView.getMeasuredHeight());
            titleViewHolderArr.put(viewType, baseViewHolder);
        }
        return baseViewHolder;
    }
}
