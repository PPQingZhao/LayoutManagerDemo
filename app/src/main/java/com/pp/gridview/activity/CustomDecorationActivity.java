package com.pp.gridview.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pp.gridview.R;
import com.pp.gridview.adapter.BaseHeaderAdapter;
import com.pp.gridview.adapter.HeaderAdapter;
import com.pp.gridview.customview.decoration.HeaderDecorration;
import com.pp.gridview.data.HeaderItemModel;

import java.util.ArrayList;
import java.util.List;

public class CustomDecorationActivity extends AppCompatActivity {

    private RecyclerView customdecoration_recyclerview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customdecoration);
        initView();
        initRecyclerView();
    }

    private void initView() {
        customdecoration_recyclerview = findViewById(R.id.customdecoration_recyclerview);
    }

    private void initRecyclerView() {
        List<HeaderItemModel> dataList = new ArrayList<>();
        HeaderItemModel headerItemModel = null;
        for (int i = 0; i < 100; i++) {
            if (i % 4 == 1) {
                headerItemModel = new HeaderItemModel(new StringBuilder("picture ").append(i).toString(), R.mipmap.ic_help_blue);
                headerItemModel.setContainTitle(true);
                dataList.add(headerItemModel);
            } else if (i % 4 == 2) {
                dataList.add(new HeaderItemModel(new StringBuilder("picture ").append(i).toString(), R.mipmap.ic_help_green));
            } else if (i % 4 == 3) {
                dataList.add(new HeaderItemModel(new StringBuilder("picture ").append(i).toString(), R.mipmap.ic_help_red));
            } else {
                dataList.add(new HeaderItemModel(new StringBuilder("picture ").append(i).toString(), R.mipmap.ic_help_green));
            }
        }
        BaseHeaderAdapter baseAdapter = new HeaderAdapter(getBaseContext(), dataList);
        HeaderDecorration aroundItemDecoration = new HeaderDecorration(Color.GREEN, 2, 2, 2, 2);
        aroundItemDecoration.attchToAdapter(baseAdapter);
        customdecoration_recyclerview.addItemDecoration(aroundItemDecoration);
        customdecoration_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        customdecoration_recyclerview.setAdapter(baseAdapter);
    }
}
