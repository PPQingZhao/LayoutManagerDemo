package com.pp.gridview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pp.gridview.adapter.BaseAdapter;
import com.pp.gridview.customview.decoration.AroundItemDecoration;
import com.pp.gridview.customview.layoutmanager.CoverFlowLayoutManager2;
import com.pp.gridview.customview.layoutmanager.GridItemTouchManager;
import com.pp.gridview.customview.gridview.GridItemTouchHelperCallback;
import com.pp.gridview.data.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView main_recyclerview;
    private BaseAdapter baseAdapter;
    private TextView main_tv_indicator;
    private CoverFlowLayoutManager2 coverFlowLayoutManager2;
    private GridItemTouchManager gridItemTouchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.pp.gridview.R.layout.activity_main);
        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        List<ItemModel> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 3 == 1) {
                list.add(new ItemModel(new StringBuilder("picture ").append(i).toString(), R.mipmap.ic_help_blue));
            } else if (i % 3 == 2) {
                list.add(new ItemModel(new StringBuilder("picture ").append(i).toString(), R.mipmap.ic_help_green));
            } else {
                list.add(new ItemModel(new StringBuilder("picture ").append(i).toString(), R.mipmap.ic_help_red));
            }
        }
        baseAdapter = new BaseAdapter(getBaseContext(), list);
        baseAdapter.setOnItemClickListener(onItemClickListener);
        coverFlowLayoutManager2 = new CoverFlowLayoutManager2(3, 5, CoverFlowLayoutManager2.HORIZONTAL);
        main_recyclerview.setLayoutManager(coverFlowLayoutManager2);
        main_recyclerview.setAdapter(baseAdapter);
        main_recyclerview.addItemDecoration(new AroundItemDecoration());
        gridItemTouchManager = new GridItemTouchManager(this, coverFlowLayoutManager2, baseAdapter);
        GridItemTouchHelperCallback gridItemTouchHelperCallback = new GridItemTouchHelperCallback(this);
        gridItemTouchHelperCallback.setOnItemTouchHelperListener(gridItemTouchManager);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(gridItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(main_recyclerview);
        main_recyclerview.post(new Runnable() {
            @Override
            public void run() {
                int[] outLocation = new int[2];
                main_recyclerview.getLocationOnScreen(outLocation);
                gridItemTouchManager.setDeleteBarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, outLocation[1]);
            }
        });
        coverFlowLayoutManager2.setOnPagerChangeListener(onPagerChangeListener);
    }

    CoverFlowLayoutManager2.OnPagerChangeListener onPagerChangeListener = new CoverFlowLayoutManager2.OnPagerChangeListener() {
        @Override
        public void onPageSelected(int position) {
            String format = String.format("%d/%d", position + 1, coverFlowLayoutManager2.getPageCount());
            main_tv_indicator.setText(format);
        }
    };

    private void initView() {
        main_tv_indicator = findViewById(R.id.main_tv_indicator);
        main_recyclerview = findViewById(R.id.main_recyclerview);
        findViewById(com.pp.gridview.R.id.main_btn).setOnClickListener(this);
        findViewById(R.id.main_btn2).setOnClickListener(this);
    }

    BaseAdapter.OnItemClickListener onItemClickListener = new BaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn:
                main_recyclerview.smoothScrollToPosition(30);
                break;
            case R.id.main_btn2:
                main_recyclerview.scrollToPosition(20);
                break;
        }
    }
}
