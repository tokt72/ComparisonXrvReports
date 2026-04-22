package com.comparison.xrv.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.comparison.xrv.model.ComparisonRecord;
import com.comparison.xrv.model.ComparisonResult;
import com.comparison.xrv.model.ComparisonStatus;
import com.comparison.xrv.model.ValidationRow;

public class ComparisonService {

    public ComparisonResult compare(
            final Map<String, List<ValidationRow>> xrvReport,
            final Map<String, List<ValidationRow>> gatewayReport
    ) {
        return compare(xrvReport, gatewayReport, "jqa_gateway");
    }

    public ComparisonResult compare(
            final Map<String, List<ValidationRow>> xrvReport,
            final Map<String, List<ValidationRow>> sourceReport,
            final String sourceName
    ) {
        final List<ComparisonRecord> records = new ArrayList<>();
        int successCount = 0;
        int failedCount = 0;

        final Set<String> allArticleIds = new HashSet<>(xrvReport.keySet());
        allArticleIds.addAll(sourceReport.keySet());

        for (String articleId : allArticleIds) {
            final List<ValidationRow> xrvValidationRows = xrvReport.getOrDefault(articleId, List.of());
            final List<ValidationRow> sourceValidationRows = sourceReport.getOrDefault(articleId, List.of());
            final List<ValidationRow> unmatchedSourceRows = new ArrayList<>(sourceValidationRows);

            for (ValidationRow xrvRow : xrvValidationRows) {
                if (removeEquivalentRow(unmatchedSourceRows, xrvRow)) {
                    records.add(toRecord(ComparisonStatus.SUCCESS, articleId, xrvRow, xrvRow, "both", ""));
                    successCount++;
                } else {
                    final int candidateIndex = findSamePositionRowIndex(unmatchedSourceRows, xrvRow);
                    if (candidateIndex >= 0) {
                        final ValidationRow sourceRow = unmatchedSourceRows.remove(candidateIndex);
                        final String diff = MessageDiffService.buildDiff(xrvRow.getMessage(), sourceRow.getMessage());
                        records.add(toRecord(ComparisonStatus.FAILED, articleId, xrvRow, sourceRow, "both", diff));
                    } else {
                        records.add(toRecord(
                                ComparisonStatus.FAILED,
                                articleId,
                                xrvRow,
                                null,
                                "xrv_service",
                                "Message exists only in xrv_service"
                        ));
                    }
                    failedCount++;
                }
            }

            for (ValidationRow sourceOnlyRow : unmatchedSourceRows) {
                records.add(toRecord(
                        ComparisonStatus.FAILED,
                        articleId,
                        null,
                        sourceOnlyRow,
                        sourceName,
                        "Message exists only in " + sourceName
                ));
                failedCount++;
            }
        }

        return ComparisonResult.of(records, successCount, failedCount);
    }

    private ComparisonRecord toRecord(
            final ComparisonStatus status,
            final String articleId,
            final ValidationRow xrvRow,
            final ValidationRow sourceRow,
            final String source,
            final String diff
    ) {
        final ValidationRow baseRow = xrvRow != null ? xrvRow : sourceRow;
        return ComparisonRecord.of(
                status,
                articleId,
                baseRow.getLevel(),
                baseRow.getRuleId(),
                baseRow.getLine(),
                baseRow.getColumn(),
                xrvRow != null ? xrvRow.getMessage() : "",
                sourceRow != null ? sourceRow.getMessage() : "",
                diff,
                source
        );
    }

    private int findSamePositionRowIndex(final List<ValidationRow> rows, final ValidationRow expected) {
        for (int i = 0; i < rows.size(); i++) {
            final ValidationRow row = rows.get(i);
            if (row.getLevel().equals(expected.getLevel())
                    && row.getRuleId().equals(expected.getRuleId())
                    && row.getLine() == expected.getLine()
                    && row.getColumn() == expected.getColumn()) {
                return i;
            }
        }
        return -1;
    }

    private boolean removeEquivalentRow(final List<ValidationRow> rows, final ValidationRow expected) {
        for (int i = 0; i < rows.size(); i++) {
            if (rowsEquivalent(rows.get(i), expected)) {
                rows.remove(i);
                return true;
            }
        }
        return false;
    }

    private boolean rowsEquivalent(final ValidationRow left, final ValidationRow right) {
        return left.getLevel().equals(right.getLevel())
                && left.getRuleId().equals(right.getRuleId())
                && left.getLine() == right.getLine()
                && left.getColumn() == right.getColumn()
                && MessageDiffService.normalizeMessage(left.getMessage())
                .equals(MessageDiffService.normalizeMessage(right.getMessage()));
    }
}
