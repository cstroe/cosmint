package com.github.cstroe.spendhawk.report;

import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.report.impl.CategoryHierarchyReport;
import com.github.cstroe.spendhawk.report.impl.PreformattedTextRenderer;
import com.github.cstroe.spendhawk.util.BaseIT;
import com.github.cstroe.spendhawk.util.Exceptions;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(Arquillian.class)
public class CategoryHierachyReportIT extends BaseIT {

    @Test
    public void testReport() throws Exception {
        startTransaction();
        User currentUser = User.findById(3l).orElseThrow(Exceptions::userNotFound);

        Category foodCategory = Category.findById(currentUser, 10l)
            .orElseThrow(Exceptions::categoryNotFound);

        Category groceryCategory = Category.findById(11l)
            .orElseThrow(Exceptions::categoryNotFound);

        assertThat(groceryCategory.getParent(), is(equalTo(foodCategory)));

        ReportRunner runner = new CategoryHierarchyReport(currentUser);
        runner.runReport();
        ReportResult result = runner.getResult();

        ReportResultRenderer renderer = new PreformattedTextRenderer(result);
        String renderOutput = renderer.render();

        assertThat(renderOutput, is("<pre>\nFood -114.0 \n> Eating Out -34.0 \n> Groceries -80.0 \n\n</pre>"));

        commitTransaction();
    }
}
