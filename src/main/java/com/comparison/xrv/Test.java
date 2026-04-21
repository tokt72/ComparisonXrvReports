package com.comparison.xrv;

import java.io.File;
import java.nio.file.Path;

import com.comparison.xrv.model.ComparisonResult;
import com.comparison.xrv.service.ComparisonReportRowMapper;
import com.comparison.xrv.service.ComparisonService;
import com.comparison.xrv.service.GatewayHtmlReadingService;
import com.comparison.xrv.service.HtmlReadingService;
import com.comparison.xrv.service.ReportService;
import com.comparison.xrv.service.XrvHtmlReadingService;

public class Test {
    private static final Path GATEWAY_DIR = Path.of("src/main/resources/jqa_gateway");
    private static final Path XRV_DIR = Path.of("src/main/resources/xrv_service");
    private static final File REPORT_FILE = new File(
            System.getProperty("user.dir") + "\\target\\report\\COMPARISON_REPORT.xlsx"
    );

    public static void main(final String[] args) {
        final HtmlReadingService gatewayReadingService = new GatewayHtmlReadingService();
        final HtmlReadingService xrvService = new XrvHtmlReadingService();
        final ComparisonService comparisonService = new ComparisonService();
        final ComparisonReportRowMapper reportRowMapper = new ComparisonReportRowMapper();
        final ReportService reportService = new ReportService();

        final var gatewayReport = gatewayReadingService.read(GATEWAY_DIR);
        final var xrvReport = xrvService.read(XRV_DIR);

        final ComparisonResult comparisonResult = comparisonService.compare(xrvReport, gatewayReport);
        reportService.writeToExcelFile(reportRowMapper.toExcelRows(comparisonResult.getRecords()), REPORT_FILE);

        System.out.println("Comparison completed. Matched rows: " + comparisonResult.getSuccessCount()
                + ", mismatched rows: " + comparisonResult.getFailedCount());
    }
}
