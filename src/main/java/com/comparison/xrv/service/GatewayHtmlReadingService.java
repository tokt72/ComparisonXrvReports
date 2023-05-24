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

public class GatewayHtmlReadingService implements HtmlReadingService {

    @SneakyThrows
    @Override
    public List<ValidationRow> read(final Path path) {
        Document document = Jsoup.parse(path.toFile(), Charset.defaultCharset().name());

        Element table = document.select("table").get(2);
        Elements rows = table.select("tr");

        List<ValidationRow> result = new ArrayList<>(rows.size() - 1);
        for (int i = 1; i < rows.size(); i++) {

            Element row = rows.get(i);
            Elements td = row.getAllElements();

            var level = td.get(2).text();
            var ruleId = td.get(4).text();
            var line = Integer.valueOf(td.get(5).text());
            var column = Integer.valueOf(td.get(6).text());
            var message = td.get(8).text();

            var validationRow = ValidationRow.of(level, ruleId, line, column, message);
            result.add(validationRow);
        }

        return result;
    }
}
