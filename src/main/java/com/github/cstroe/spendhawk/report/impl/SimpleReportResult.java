package com.github.cstroe.spendhawk.report.impl;

import com.github.cstroe.spendhawk.report.ReportResult;

public class SimpleReportResult implements ReportResult {

    private final int rows;
    private final int cols;
    private final String[][] data;

    public SimpleReportResult(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new String[rows][cols];
    }

    @Override
    public int getNumRows() {
        return rows;
    }

    @Override
    public int getNumColumns() {
        return cols;
    }

    @Override
    public String getCell(int row, int col) {
        return data[row][col];
    }

    public void setCell(int row, int col, String value) {
        data[row][col] = value;
    }
}
