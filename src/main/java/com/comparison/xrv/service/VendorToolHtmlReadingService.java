package com.comparison.xrv.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VendorToolHtmlReadingService extends SeparatingHtmlReadingService {

    @Override
    protected Element getTable(final Document document) {
        return document.getElementsByTag(TABLE).first();
    }

    @Override
    protected String getLevelByTd(final Elements td) {
        return td.get(2).text();
    }

    @Override
    protected String getRuleIdByTd(final Elements td) {
        return td.get(4).text();
    }

    @Override
    protected int getLineByTd(final Elements td) {
        return Integer.valueOf(td.get(5).text());
    }

    @Override
    protected int getColumnByTd(final Elements td) {
        return Integer.valueOf(td.get(6).text());
    }

    @Override
    protected String getMessageByTd(final Elements td) {
        return td.get(8).text();
    }
}