package com.github.cstroe.spendhawk.report.impl;

import com.github.cstroe.spendhawk.report.ReportResult;
import com.github.cstroe.spendhawk.report.ReportResultRenderer;

/**
 * Renders a result as an HTML table.
 */
public class TableRenderer implements ReportResultRenderer {

    private final ReportResult result;

    public TableRenderer(ReportResult result) {
        this.result = result;
    }

    @Override
    public String render() {
        StringBuilder builder = new StringBuilder();
        builder.append("<table border=\"1\">\n");
        for(int i = 0; i < result.getNumRows(); i++) {
            builder.append("\t<tr>\n");
            for(int j = 0; j < result.getNumColumns(); j++) {
                builder.append("\t\t<td>\n");
                builder.append("\t\t\t").append(result.getCell(i,j));
                builder.append("\t\t</td>\n");
            }
            builder.append("\t</tr>\n");
        }
        builder.append("</table>");

        return builder.toString();
    }
}
