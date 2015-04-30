package com.github.cstroe.spendhawk.report;

/**
 * A parameter to a report.
 */
public class ReportParameter {

    public enum ReportParameterType {
        DATE
    }

    private final String id;
    private final String displayName;
    private final ReportParameterType type;
    private String value;

    public ReportParameter(String id, String displayName, ReportParameterType type) {
        this.id = id;
        this.displayName = displayName;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ReportParameterType getType() {
        return type;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
