package com.github.cstroe.spendhawk.report;

import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ReportFormGenerator {

    private final User currentUser;
    private final List<ReportParameter> parameterList;

    public ReportFormGenerator(User currentUser, List<ReportParameter> parameterList) {
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

                case CATEGORY:
                    out.append(parameter.getDisplayName())
                       .append(": ")
                       .append("<select name=\"")
                       .append(parameter.getId())
                       .append("\">\n");

                    List<Category> categories = Category.findAll(currentUser);

                    for(Category category : categories) {
                        out.append("<option value=\"")
                           .append(category.getId())
                           .append("\">")
                           .append(category.getName())
                           .append("</option>\n");
                    }

                    out.append("</select><br/>");
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
