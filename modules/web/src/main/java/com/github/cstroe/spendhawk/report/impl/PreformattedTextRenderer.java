package com.github.cstroe.spendhawk.report.impl;

import com.github.cstroe.spendhawk.report.ReportResult;
import com.github.cstroe.spendhawk.report.ReportResultRenderer;

public class PreformattedTextRenderer implements ReportResultRenderer {

    private final ReportResult result;

    public PreformattedTextRenderer(ReportResult result) {
        this.result = result;
    }

    @Override
    public String render() {
        StringBuilder out = new StringBuilder();
        out.append("<pre>\n");
        for(int i = 0; i < result.getNumRows(); i++) {
            for(int j = 0; j < result.getNumColumns(); j++) {
                out.append(result.getCell(i, j))
                   .append(" ");
            }
            out.append("\n");
        }
        out.append("\n</pre>");
        return out.toString();
    }
}
