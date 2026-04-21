package com.comparison.xrv.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ComparisonRecord {
    private final ComparisonStatus status;
    private final String articleId;
    private final String level;
    private final String rule;
    private final int line;
    private final int column;
    private final String messageXrv;
    private final String messageJqa;
    private final String messageDiff;
    private final String source;
}
