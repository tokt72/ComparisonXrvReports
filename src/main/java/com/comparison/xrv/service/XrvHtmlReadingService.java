package com.comparison.xrv.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class XrvHtmlReadingService extends SeparatingHtmlReadingService {
    private static final String TABLE_CSS_CLASSES = "xrvresult xrvresultMessages";

    @Override
    protected Element getTable(final Document document) {
        return document.getElementsByClass(TABLE_CSS_CLASSES).first();
    }

    @Override
    protected String getLevelByTd(final Elements td) {
        return td.get(3).text();
    }

    @Override
    protected String getRuleIdByTd(final Elements td) {
        return td.get(5).text();
    }

    @Override
    protected int getLineByTd(final Elements td) {
        return Integer.valueOf(td.get(6).text());
    }

    @Override
    protected int getColumnByTd(final Elements td) {
        return Integer.valueOf(td.get(7).text());
    }

    @Override
    protected String getMessageByTd(final Elements td) {
        return td.get(9).text();
    }
}