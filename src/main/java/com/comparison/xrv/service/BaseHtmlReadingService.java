package com.comparison.xrv.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comparison.xrv.model.ValidationRow;

import lombok.SneakyThrows;

abstract class BaseHtmlReadingService implements HtmlReadingService {
    protected static final String TABLE = "table";
    protected static final String TR = "tr";

    @SneakyThrows
    @Override
    public Map<String, List<ValidationRow>> read(final Path dir) {
        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException(dir + " is not are directory");
        }
        final Map<String, List<ValidationRow>> result = new HashMap<>();
        Files.walk(dir)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    final var filename = path.getFileName().toString();
                    final var separator = filename.indexOf("_");
                    if (separator == -1) {
                        throw new IllegalArgumentException("Incorrect filename " + filename);
                    }
                    final var articleId = filename.substring(0, separator).toUpperCase();
                    final var rows = readHtmlFile(path);
                    result.computeIfAbsent(articleId, a -> new ArrayList<>()).addAll(rows);
                });
        return result;
    }

    protected abstract List<ValidationRow> readHtmlFile(Path file);
}