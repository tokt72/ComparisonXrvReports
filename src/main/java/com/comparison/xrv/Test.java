package com.comparison.xrv;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import com.comparison.xrv.service.GatewayHtmlReadingService;
import com.comparison.xrv.service.HtmlReadingService;
import com.comparison.xrv.service.ReportService;
import com.comparison.xrv.service.VendorToolHtmlReadingService;
import com.comparison.xrv.service.XrvHtmlReadingService;

public class Test {
    public static void main(final String[] args) {
        final HtmlReadingService gatewayReadingService = new GatewayHtmlReadingService();
        final HtmlReadingService xrvService = new XrvHtmlReadingService();
        final HtmlReadingService vendorToolService = new VendorToolHtmlReadingService();

        final ReportService reportService = new ReportService();

        final var gatewayReport = gatewayReadingService.read(Path.of(
                "src/main/resources/jqa_gateway"
        ));
        final var xrvReport = xrvService.read(Path.of(
                "src/main/resources/xrv_service"
        ));
//        final var vendorToolReport = vendorToolService.read(Path.of(
//                "src/main/resources/vendor_tool"
//        ));

        assertEquals(xrvReport.keySet(), gatewayReport.keySet());

        List<String[]> success = new ArrayList<>();
        String[] headerSuccess = {"articleId", "Level", "Rule", "Line", "Column", "Message"};
        success.add(headerSuccess);

        List<String[]> failed = new ArrayList<>();
        String[] headerFailed = {"articleId", "Level", "Rule", "Line", "Column", "Message"};
        failed.add(headerFailed);

        for (String key : xrvReport.keySet()) {
            var xrvValidationRows = xrvReport.get(key);
            var gatewayValidationRows = gatewayReport.get(key);

            assertEquals(xrvValidationRows.size(), gatewayValidationRows.size());
            assertEquals(gatewayValidationRows.size(), gatewayValidationRows.size());

            for (int i = 0; i < xrvValidationRows.size(); i++) {
                final var row = xrvValidationRows.get(i);
                final String[] line = new String[]{
                        row.getArticleID(),
                        row.getLevel(),
                        row.getRuleId(),
                        String.valueOf(row.getLine()),
                        String.valueOf(row.getColumn()),
                        row.getMessage()
                };
                if (xrvValidationRows.contains(gatewayValidationRows.get(i))) {
                    success.add(line);
                } else {
                    failed.add(line);
                }
            }

            if (success.size() > 1) {
                reportService.writeToCsvFile(success, new File(System.getProperty("user.dir")
                        + "\\target\\report\\SUCCESS.csv"));

            } else if (failed.size() > 1) {
                reportService.writeToCsvFile(failed, new File(System.getProperty("user.dir")
                        + "\\target\\report\\FAILED.csv"));
            }
        }
    }
}
