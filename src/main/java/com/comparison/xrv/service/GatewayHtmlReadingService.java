package com.comparison.xrv.service;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.comparison.xrv.model.ValidationRow;

import lombok.SneakyThrows;

public class GatewayHtmlReadingService extends BaseHtmlReadingService {
    @SneakyThrows
    @Override
    public List<ValidationRow> readHtmlFile(final Path path) {
        final Document document = Jsoup.parse(path.toFile(), Charset.defaultCharset().name());

        final Element table = document.select(TABLE).last();
        final Elements rows = table.select(TR);

        final List<ValidationRow> result = new ArrayList<>(rows.size() - 1);
        for (int i = 1; i < rows.size(); i++) {
            final Elements td = rows.get(i).getAllElements();

            final var filename = path.getFileName().toString();
            final var separator = filename.indexOf("_");
            if (separator == -1) {
                throw new IllegalArgumentException("Incorrect filename " + filename);
            }

            final var articleId =  path.getFileName().toString().substring(0, separator).toUpperCase();
            final var level = td.get(2).text();
            final var ruleId = td.get(4).text();
            final int line = Integer.parseInt(td.get(5).text().trim());
            final int column = Integer.parseInt(td.get(6).text().trim());
            final var message = td.get(8).text();

            final var validationRow = ValidationRow.of(articleId, level, ruleId, line, column, message);
            result.add(validationRow);
        }

        return result;
    }
}