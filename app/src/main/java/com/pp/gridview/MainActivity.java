package com.pp.gridview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.pp.gridview.adapter.BaseAdapter;
import com.pp.gridview.customview.CoverFlowLayoutManager;
import com.pp.gridview.customview.CoverFlowLayoutManager2;
import com.pp.gridview.customview.OnGridItemTouchHelperListener;
import com.pp.gridview.customview.SingleGridView;
import com.pp.gridview.customview.deletebar.DeleteBarView;
import com.pp.gridview.customview.gridview.GridItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView main_recyclerview;
    private CoverFlowLayoutManager coverFlowLayoutManager;
    private SingleGridView main_singlegridview;
    private WindowManager.LayoutParams deletedBarParams;
    private DeleteBarView deleteBarView;
    private WindowManager windowManager;
    private GridItemTouchHelperCallback gridItemTouchHelperCallback;
    private BaseAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.pp.gridview.R.layout.activity_main);
        initView();
        initGridView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("position: " + i);
        }
        baseAdapter = new BaseAdapter(getBaseContext(), list);
        baseAdapter.setOnItemClickListener(onItemClickListener);
//        CoverFlowLayoutManager2 coverFlowLayoutManager2 = new CoverFlowLayoutManager2(3, 5,CoverFlowLayoutManager2.VERTICAL);
        CoverFlowLayoutManager2 coverFlowLayoutManager2 = new CoverFlowLayoutManager2(3, 5,CoverFlowLayoutManager2.HORIZONTAL);
        CoverFlowLayoutManager coverFlowLayoutManager = new CoverFlowLayoutManager();
//        main_recyclerview.setLayoutManager(coverFlowLayoutManager2);
        main_recyclerview.setLayoutManager(coverFlowLayoutManager2);
        main_recyclerview.setAdapter(baseAdapter);
        gridItemTouchHelperCallback = new GridItemTouchHelperCallback(this);
        gridItemTouchHelperCallback.setOnItemTouchHelperListener(new OnGridItemTouchHelperListener(coverFlowLayoutManager2,baseAdapter));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(gridItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(main_recyclerview);
        main_recyclerview.post(new Runnable() {
            @Override
            public void run() {
                int[] outLocation = new int[2];
                main_recyclerview.getLocationOnScreen(outLocation);
                gridItemTouchHelperCallback.setDeleteBarLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, outLocation[1]);
            }
        });
        coverFlowLayoutManager2.setOnPagerChangeListener(new CoverFlowLayoutManager2.OnPagerChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.e("TAG","******************** onPageSelected: " + position);
            }
        });
    }

    private void initGridView() {
        main_singlegridview = findViewById(R.id.main_singlegridview);
        main_singlegridview.setLayoutModel(SingleGridView.Model_4_4_8);
        for (int i = 0; i < main_singlegridview.getShowCount(); i++) {
            TextView textView = new TextView(getBaseContext());
            textView.setGravity(Gravity.CENTER);
            textView.setText("" + i);
            if (i % 2 == 0) {
                textView.setBackgroundColor(Color.BLUE);
            } else if (i % 3 == 0) {
                textView.setBackgroundColor(Color.YELLOW);
            } else if (i % 4 == 0) {
                textView.setBackgroundColor(Color.GREEN);
            } else if (i % 5 == 0) {
                textView.setBackgroundColor(Color.GRAY);
            } else if (i % 6 == 0) {
                textView.setBackgroundColor(Color.GREEN);
            } else {
                textView.setBackgroundColor(Color.YELLOW);
            }
            main_singlegridview.addView(textView);
        }
    }

    private void initView() {
        findViewById(com.pp.gridview.R.id.main_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_recyclerview.smoothScrollToPosition(40);
            }
        });
        findViewById(R.id.main_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_recyclerview.scrollToPosition(20);
            }
        });
        main_recyclerview = findViewById(com.pp.gridview.R.id.main_recyclerview);
    }

    BaseAdapter.OnItemClickListener onItemClickListener = new BaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Log.e("TAG", "****************  position: " + position);
        }
    };
}
