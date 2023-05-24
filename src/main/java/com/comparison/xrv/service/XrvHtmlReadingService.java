package com.comparison.xrv.service;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.comparison.xrv.model.ValidationRow;

import lombok.SneakyThrows;

public class XrvHtmlReadingService implements HtmlReadingService {

    @SneakyThrows
    @Override
    public Map<Integer, List<ValidationRow>> read(final Path path) {
        Document document;

        //Get Document object after parsing the html file.
        document = Jsoup.parse(path.toFile(), "UTF-8");

        Element table = document.select("table").get(1);
        Elements rows = table.select("tr");

        HashMap<Integer, List<String>> values = new HashMap<>();
        for (int i = 0; i < rows.size(); i++) {

            Element row = rows.get(i);
            Elements td = row.getAllElements();

            var fields = List.of(td.get(2).text(), td.get(4).text(), td.get(5).text(), td.get(6).text(),
                    td.get(8).text());

            values.remove(1);
            values.put(i, fields);
        }

        return null;
    }
}
