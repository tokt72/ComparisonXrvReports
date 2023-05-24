package com.comparison.xrv.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ValidationRow {
    private final String level;
    private final String ruleId;
    private final int line;
    private final int column;
    private final String message;
}
