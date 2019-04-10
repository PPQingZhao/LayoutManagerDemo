package com.pp.gridview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.pp.gridview.R.layout.activity_main);
        initView();
        initGridView();
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
            }
        });
        main_recyclerview = findViewById(com.pp.gridview.R.id.main_recyclerview);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            list.add("position: " + i);
        }
        BaseAdapter baseAdapter = new BaseAdapter(getBaseContext(), list);
        baseAdapter.setOnItemClickListener(onItemClickListener);
        coverFlowLayoutManager = new CoverFlowLayoutManager();
//        main_recyclerview.setLayoutManager(new LinearLayoutManager(getBaseContext()));
//        main_recyclerview.setLayoutManager(coverFlowLayoutManager);
        main_recyclerview.setLayoutManager(new CoverFlowLayoutManager2(3, 3));
        main_recyclerview.setAdapter(baseAdapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setSupportsChangeAnimations(false);
        main_recyclerview.setItemAnimator(defaultItemAnimator);
        gridItemTouchHelperCallback = new GridItemTouchHelperCallback(this);
        gridItemTouchHelperCallback.setOnItemTouchHelperListener(baseAdapter);
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
    }

    BaseAdapter.OnItemClickListener onItemClickListener = new BaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Log.e("TAG", "****************  position: " + position);
        }
    };
}
