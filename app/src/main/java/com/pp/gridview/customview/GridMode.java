package com.pp.gridview.customview;

public class GridMode {
    private final int columCount; //行
    private final int rowCount;   //列
    private final int showCount;  //显示的个数

    public GridMode(int columCount, int rowCount, int showCount) {
        this.columCount = columCount;
        this.rowCount = rowCount;
        this.showCount = showCount;
    }

    public int getColumCount() {
        return columCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getShowCount() {
        return showCount;
    }
}
