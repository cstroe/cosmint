package com.github.cstroe.spendhawk.report;

import com.github.cstroe.spendhawk.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Generate the HTML form for inputting report parameters.
 */
public class ReportFormGenerator {

    private final UserDao currentUser;
    private final List<ReportParameter> parameterList;

    public ReportFormGenerator(UserDao currentUser, List<ReportParameter> parameterList) {
        this.currentUser = currentUser;
        this.parameterList = parameterList;
    }

    public String getForm() {
        StringBuilder out = new StringBuilder();

        for(ReportParameter parameter : parameterList) {
            switch(parameter.getType()) {
                case DATE:
                    out.append(parameter.getDisplayName())
                       .append(": ")
                       .append("<input type=\"text\" name=\"")
                       .append(parameter.getId())
                       .append("\"/><br/>");
                    break;

                default:
                    out.append(parameter.getDisplayName())
                       .append("?<br/>");
            }
        }

        return out.toString();
    }

    public void parseParameters(HttpServletRequest req, List<ReportParameter> parameters) {
        for(ReportParameter parameter : parameters) {
            String parameterValue = req.getParameter(parameter.getId());
            parameter.setValue(parameterValue);
        }
    }
}
