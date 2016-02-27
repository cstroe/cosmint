package com.github.cstroe.spendhawk.report;

import java.util.List;

/**
 * Runs a report.
 */
public interface ReportRunner {
    /**
     * @return A short description of the report.
     */
    public String getName();

    /**
     * @return A human readable description of what the report outputs.
     */
    public String getHelpfulDescription();

    /**
     * @return A list of parameters that this report needs to run.
     */
    public List<ReportParameter> getParameters();

    /**
     * Runs the report, after which a result can be retrieved.
     */
    public void runReport() throws Exception;

    public ReportResult getResult();

    /** Helper method to make code shorter. */
    default ReportParameter rp(String id, String displayName, ReportParameter.ReportParameterType type) {
        return new ReportParameter(id, displayName, type);
    }

}
