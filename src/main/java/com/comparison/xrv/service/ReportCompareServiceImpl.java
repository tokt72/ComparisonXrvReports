package com.comparison.xrv.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import com.comparison.xrv.model.ValidationRow;

import lombok.SneakyThrows;

public class ReportCompareServiceImpl implements ReportCompareService {
    @SneakyThrows
    @Override
    public List<String> compareReports(final Function<Path, List<ValidationRow>>...handlers) {
        Map<String, List<ValidationRow>>
        for (Function<Path, List<ValidationRow>> function: handlers) {
            function.apply()
            Files.walk(dir).forEach(path -> {
                var filename = path.getFileName().toString();
                var separator = filename.indexOf("_");
                if(separator == -1){
                    throw new IllegalArgumentException("Incorrect filename " + filename);
                }
                var articleId = filename.substring(0, separator);
            });
        }
        return null;
    }
}
