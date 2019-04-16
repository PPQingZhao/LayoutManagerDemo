package com.pp.gridview.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.pp.gridview.R;
import com.pp.gridview.adapter.BaseTitleMultiItemAdapter;
import com.pp.gridview.adapter.VHBaseAdapter;
import com.pp.gridview.customview.decoration.BaseVHTitleDecoration;
import com.pp.gridview.data.VHelperLevel0;
import com.pp.gridview.data.VHelperLevel1;

import java.util.ArrayList;
import java.util.List;

public class BaseVHDecorationActivity extends AppCompatActivity {

    private RecyclerView basevhdecoration_recyclerview;
    private BaseTitleMultiItemAdapter<MultiItemEntity> baseVHAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basevhdecoration);
        initView();
        initRecyclerView();
    }

    private void initView() {
        basevhdecoration_recyclerview = findViewById(R.id.basevhdecoration_recyclerview);
    }

    private void initRecyclerView() {
        final List<MultiItemEntity> dataList = new ArrayList<>();
        VHelperLevel0 vHelperLevel0 = null;
        VHelperLevel1 vHelperLevel1 = null;
        for (int i = 0; i < 50; i++) {
            vHelperLevel0 = new VHelperLevel0(new StringBuilder("Group ").append(i).toString());
            for (int j = 0; j < i * (i % 3); j++) {
                if (j % 3 == 1) {
                    vHelperLevel1 = new VHelperLevel1(new StringBuilder(vHelperLevel0.getLevelName()).append("Item").append(j).toString(), R.mipmap.ic_help_red);
                } else if (j % 3 == 2) {
                    vHelperLevel1 = new VHelperLevel1(new StringBuilder(vHelperLevel0.getLevelName()).append("Item").append(j).toString(), R.mipmap.ic_help_green);
                } else {
                    vHelperLevel1 = new VHelperLevel1(new StringBuilder(vHelperLevel0.getLevelName()).append("Item").append(j).toString(), R.mipmap.ic_help_blue);
                }
                vHelperLevel0.addSubItem(vHelperLevel1);
            }
            dataList.add(vHelperLevel0);
        }
        baseVHAdapter = new VHBaseAdapter(dataList);
        BaseVHTitleDecoration aroundItemDecoration = new BaseVHTitleDecoration(Color.GREEN, 0, 2, 0, 0);
        aroundItemDecoration.attachAdapter(baseVHAdapter);
        basevhdecoration_recyclerview.addItemDecoration(aroundItemDecoration);
        basevhdecoration_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        basevhdecoration_recyclerview.setAdapter(baseVHAdapter);
        baseVHAdapter.expandAll();
        baseVHAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity item = baseVHAdapter.getItem(position);
                switch (item.getItemType()) {
                    case BaseTitleMultiItemAdapter.TYPE_LEVEL_0:
                        VHelperLevel0 helperLevel0 = (VHelperLevel0) item;
                        if (helperLevel0.isExpanded()) {
                            baseVHAdapter.collapse(position);
                        } else {
                            baseVHAdapter.expand(position);
                        }
                        break;
                    case BaseTitleMultiItemAdapter.TYPE_LEVEL_1:
                        VHelperLevel1 vHelperLevel1 = (VHelperLevel1) item;
                        Toast.makeText(getApplicationContext(), new StringBuilder(vHelperLevel1.getName()), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
