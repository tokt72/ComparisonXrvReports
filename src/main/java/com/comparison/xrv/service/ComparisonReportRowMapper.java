package com.comparison.xrv.service;

import java.util.ArrayList;
import java.util.List;

import com.comparison.xrv.model.ComparisonRecord;

public class ComparisonReportRowMapper {
    public List<String[]> toExcelRows(final List<ComparisonRecord> records) {
        return toExcelRows(records, "JQA");
    }

    public List<String[]> toExcelRows(final List<ComparisonRecord> records, final String sourceLabel) {
        final List<String[]> rows = new ArrayList<>(records.size() + 1);
        rows.add(new String[]{
                "Result",
                "ArticleId",
                "Level",
                "Rule",
                "Line",
                "Column",
                "Message XRV",
                "Message " + sourceLabel,
                "Message Diff",
                "Source"
        });

        for (ComparisonRecord record : records) {
            rows.add(new String[]{
                    record.getStatus().name(),
                    record.getArticleId(),
                    record.getLevel(),
                    record.getRule(),
                    String.valueOf(record.getLine()),
                    String.valueOf(record.getColumn()),
                    record.getMessageXrv(),
                    record.getMessageJqa(),
                    record.getMessageDiff(),
                    record.getSource()
            });
        }

        return rows;
    }
}
