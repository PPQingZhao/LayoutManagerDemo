package com.pp.gridview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pp.gridview.activity.BaseVHDecorationActivity;
import com.pp.gridview.activity.CustomDecorationActivity;
import com.pp.gridview.activity.CustomLayoutManagerActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLayoutManager(View view) {
        Intent intent = new Intent(getBaseContext(), CustomLayoutManagerActivity.class);
        startActivity(intent);
    }

    public void onDecortion(View view) {
        Intent intent = new Intent(getBaseContext(), CustomDecorationActivity.class);
        startActivity(intent);
    }

    public void onViewHolderHelper(View view) {
        Intent intent = new Intent(getBaseContext(), BaseVHDecorationActivity.class);
        startActivity(intent);
    }
}
