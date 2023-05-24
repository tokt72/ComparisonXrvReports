package com.comparison.xrv.service;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.comparison.xrv.model.ValidationRow;

import lombok.SneakyThrows;

abstract class SeparatingHtmlReadingService extends BaseHtmlReadingService {
    private static final String STRING_SEPARATOR = "Also occurs at these line:column locations: ";

    @SneakyThrows
    @Override
    public List<ValidationRow> readHtmlFile(final Path path) {
        final Document document = Jsoup.parse(path.toFile(), Charset.defaultCharset().name());

        final Element table = getTable(document);
        final Elements rows = table.select(TR);

        final List<ValidationRow> result = new ArrayList<>(rows.size() - 1);

        for (int i = 1; i < rows.size(); i++) {
            final Elements td = rows.get(i).getAllElements();

            final var level = getLevelByTd(td);
            final var ruleId = getRuleIdByTd(td);
            final int line = getLineByTd(td);
            final int column = getColumnByTd(td);
            final var message = getMessageByTd(td);

            final ValidationRow validationRow;
            if (message.contains(STRING_SEPARATOR)) {
                final var separatedMessage = message.split(STRING_SEPARATOR);
                final var newMessage = separatedMessage[0];
                final var stringTokenizer = new StringTokenizer(separatedMessage[1], ";.");

                while (stringTokenizer.hasMoreTokens()) {
                    final String[] nextToken = stringTokenizer.nextToken().split(":");
                    final int newLine = Integer.valueOf(nextToken[0].trim());
                    final int newColumn = Integer.valueOf(nextToken[1].trim());

                    final var additionRow = ValidationRow.of(level, ruleId, newLine, newColumn, newMessage);
                    result.add(additionRow);
                }

                validationRow = ValidationRow.of(level, ruleId, line, column, newMessage);
            } else {
                validationRow = ValidationRow.of(level, ruleId, line, column, message);
            }

            result.add(validationRow);
        }

        return result;
    }

    protected abstract Element getTable(Document document);

    protected abstract String getLevelByTd(Elements td);

    protected abstract String getRuleIdByTd(Elements td);

    protected abstract int getLineByTd(Elements td);

    protected abstract int getColumnByTd(Elements td);

    protected abstract String getMessageByTd(Elements td);
}