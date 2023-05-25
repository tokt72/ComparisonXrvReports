package com.comparison.xrv.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.SneakyThrows;


public class ReportService {

    private static final String COMMA = ",";
    private static final String DEFAULT_SEPARATOR = COMMA;
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMBEDDED_DOUBLE_QUOTES = "\"\"";
    private static final String NEW_LINE_UNIX = "\n";
    private static final String NEW_LINE_WINDOWS = "\r\n";

    public String convertToCsvFormat(final String[] line) {
        return convertToCsvFormat(line, DEFAULT_SEPARATOR);
    }

    public String convertToCsvFormat(final String[] line, final String separator) {
        return convertToCsvFormat(line, separator, true);
    }

    public String convertToCsvFormat(
            final String[] line,
            final String separator,
            final boolean quote) {

        return Stream.of(line)
                .map(l -> formatCsvField(l, quote))
                .collect(Collectors.joining(separator));
    }

    private String formatCsvField(final String field, final boolean quote) {
        String result = field;

        if (result.contains(COMMA)
                || result.contains(DOUBLE_QUOTES)
                || result.contains(NEW_LINE_UNIX)
                || result.contains(NEW_LINE_WINDOWS)) {

            result = result.replace(DOUBLE_QUOTES, EMBEDDED_DOUBLE_QUOTES);

            result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;

        } else {
            if (quote) {
                result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;
            }
        }

        return result;
    }

    @SneakyThrows
    public void writeToCsvFile(List<String[]> list, File file) {

        List<String> collect = list.stream()
                .map(this::convertToCsvFormat)
                .collect(Collectors.toList());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : collect) {
                bw.write(line);
                bw.newLine();
            }
        }
    }
}
