package com.comparison.xrv.service;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import com.comparison.xrv.model.ValidationRow;

public interface ReportCompareService {
    List<String> compareReports(Function<Path, List<ValidationRow>>...handlers);
}
