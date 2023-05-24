package com.comparison.xrv.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;

public class HtmlReadingServiceImpl {

    public HashMap<Integer, List<String>> readFileFromXrv(int tableNumber, String path) throws IOException {
        File inputFile = new File(path);
        Document document;

        //Get Document object after parsing the html file.
        document = Jsoup.parse(inputFile, "UTF-8");

        Element table = document.select("table").get(tableNumber);
        Elements rows = table.select("tr");

        HashMap<Integer, List<String>> values = new HashMap<>();

        List<String> fields;
        List<String> fields2;

        for (int i = 0; i < rows.size(); i++) {

            Element row = rows.get(i);
            Elements td = row.getAllElements();

            //Case with one lines
            if (td.get(9).text().contains("Also occurs at these line:column locations: ")) {

                var split = td.get(9).text().split("Also occurs at these line:column locations: ");
                List<String> targetList = Lists.newArrayList(split);

                List<String> lineAndColumn = Lists.newArrayList(targetList.get(1).replace(".", "")
                        .split(":"));

                fields = List.of(td.get(3).text(), td.get(5).text(), lineAndColumn.get(0), lineAndColumn.get(1),
                        targetList.get(0));

                fields2 = List.of(td.get(3).text(), td.get(5).text(), td.get(6).text(), td.get(7).text(),
                        targetList.get(0));

                values.put(i, fields);
                values.put(values.size() + 1, fields2);
            }

            //Case with more one lines
            if (td.get(9).text().contains("Also occurs at these line:column locations: ") && td.get(9).text()
                    .contains(";")) {

                var split = td.get(9).text().split("Also occurs at these line:column locations: ");
                List<String> targetList = Lists.newArrayList(split);
                List<String> linesAndColumns = Lists.newArrayList(targetList.get(1).replace(".", "")
                        .replace(" ", "").split(";"));

                if (rows.size() != 2) {
                    for (int j = 0; j < linesAndColumns.size(); j++) {
                        List<String> linesAndColumns2 = Lists.newArrayList(linesAndColumns.get(j).split(":"));
                        fields = List.of(td.get(3).text(), td.get(5).text(), linesAndColumns2.get(0), linesAndColumns2.get(1),
                                targetList.get(0));

                        fields2 = List.of(td.get(3).text(), td.get(5).text(), td.get(6).text(), td.get(7).text(),
                                targetList.get(0));

                        values.put(j, fields);
                        values.put(values.size() + 1, fields2);
                    }
                }

                if (rows.size() == 2) {
                    for (int j = 0; j < linesAndColumns.size(); j++) {
                        List<String> linesAndColumns2 = Lists.newArrayList(linesAndColumns.get(j).split(":"));
                        fields = List.of(td.get(3).text(), td.get(5).text(), linesAndColumns2.get(0), linesAndColumns2.get(1),
                                targetList.get(0));
                        values.put(j, fields);
                    }
                    fields2 = List.of(td.get(3).text(), td.get(5).text(), td.get(6).text(), td.get(7).text(),
                            targetList.get(0));
                    values.put(values.size() + 1, fields2);
                }
            } else if (!td.get(9).text().contains("Also occurs at these line:column locations: ")) {

                fields = List.of(td.get(3).text(), td.get(5).text(), td.get(6).text(), td.get(7).text(),
                        td.get(9).text());

                values.put(i, fields);
            }
        }

        return values;
    }

    public HashMap<Integer, List<String>> readFileFromJqa(int tableNumber, String path) throws IOException {
        File inputFile = new File(path);
        Document document;

        //Get Document object after parsing the html file.
        document = Jsoup.parse(inputFile, "UTF-8");

        Element table = document.select("table").get(tableNumber);
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

        return values;
    }
}
