package com.github.cstroe.spendhawk.report;

public interface ReportResult {
    public int getNumRows();
    public int getNumColumns();
    public String getCell(int row, int col);
}
