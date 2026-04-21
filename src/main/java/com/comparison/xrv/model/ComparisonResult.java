package com.comparison.xrv.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ComparisonResult {
    private final List<ComparisonRecord> records;
    private final int successCount;
    private final int failedCount;
}
