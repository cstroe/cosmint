package com.github.cstroe.spendhawk.report.impl;

import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.Expense;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.report.ReportParameter;
import com.github.cstroe.spendhawk.report.ReportResult;
import com.github.cstroe.spendhawk.report.ReportRunner;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CategoryHierarchyReport implements ReportRunner {

    private final User currentUser;
    private SimpleReportResult result;

    @SuppressWarnings("unused")
    public CategoryHierarchyReport(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public String getName() {
        return "Category Hierarchy Report";
    }

    @Override
    public String getHelpfulDescription() {
        return "Print out the item hierarchy.";
    }

    @Override
    public List<ReportParameter> getParameters() {
        return Collections.emptyList();
    }

    @Override
    public void runReport() throws Exception {
        List<Category> categories = Category.findAll(currentUser);

        List<Category> rootNodes = categories.stream().sorted()
            .filter(c -> c.getParent() == null).collect(Collectors.toList());

        SimpleReportResult res = new SimpleReportResult(categories.size(), 2);

        int currentRow = 0;
        int currentRootIndex = 0;
        while(currentRootIndex < rootNodes.size()) {
            Category currentRoot = rootNodes.get(currentRootIndex);
            int delta = fillIn(res, currentRow, currentRoot);
            currentRow += delta;
            currentRootIndex++;
        }

        result = res;
    }

    private int fillIn(SimpleReportResult res, int currentRow, Category currentRoot) {
        int categoryDepth = getDepth(currentRoot);
        double categoryTotal = computeCategoryTotal(currentRoot);
        res.setCell(currentRow, 0, strRepeat("> ", categoryDepth) + currentRoot.getName());
        res.setCell(currentRow, 1, Double.toString(categoryTotal));
        int delta = 1;
        final List<Category> sortedChildren = currentRoot.getChildren().stream()
            .sorted().collect(Collectors.toList());
        for(Category currentRootChild : sortedChildren) {
            delta += fillIn(res, currentRow + delta, currentRootChild);
        }
        return delta;
    }

    private double computeCategoryTotal(Category category) {
        List<Expense> expenses = Expense.findByCategory(category);
        double sum = expenses.stream().mapToDouble(Expense::getAmount).sum();
        for(Category child : category.getChildren()) {
            sum += computeCategoryTotal(child);
        }
        return sum;
    }

    private int getDepth(Category category) {
        if(category.getParent() == null) {
            return 0;
        }
        return 1 + getDepth(category.getParent());
    }

    private String strRepeat(String string, int numberOfRepeats) {
        StringBuilder b = new StringBuilder();
        for(int i = 0; i < numberOfRepeats; i++) {
            b.append(string);
        }
        return b.toString();
    }

    @Override
    public ReportResult getResult() {
        return result;
    }
}
