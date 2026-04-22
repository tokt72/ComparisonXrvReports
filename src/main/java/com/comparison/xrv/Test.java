package com.comparison.xrv;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.comparison.xrv.model.ComparisonResult;
import com.comparison.xrv.service.ComparisonReportRowMapper;
import com.comparison.xrv.service.ComparisonService;
import com.comparison.xrv.service.GatewayHtmlReadingService;
import com.comparison.xrv.service.HtmlReadingService;
import com.comparison.xrv.service.ReportService;
import com.comparison.xrv.service.VendorToolHtmlReadingService;
import com.comparison.xrv.service.XrvHtmlReadingService;

public class Test {
    private static final Path GATEWAY_DIR = Path.of("src/main/resources/jqa_gateway");
    private static final Path VENDOR_TOOL_DIR = Path.of("src/main/resources/vendor_tool");
    private static final Path XRV_DIR = Path.of("src/main/resources/xrv_service");
    private static final File REPORT_FILE = new File(
            System.getProperty("user.dir") + "\\target\\report\\COMPARISON_REPORT.xlsx"
    );

    public static void main(final String[] args) {
        final HtmlReadingService gatewayReadingService = new GatewayHtmlReadingService();
        final HtmlReadingService vendorToolReadingService = new VendorToolHtmlReadingService();
        final HtmlReadingService xrvService = new XrvHtmlReadingService();
        final ComparisonService comparisonService = new ComparisonService();
        final ComparisonReportRowMapper reportRowMapper = new ComparisonReportRowMapper();
        final ReportService reportService = new ReportService();

        final var xrvReport = xrvService.read(XRV_DIR);
        final Map<String, List<String[]>> sheets = new LinkedHashMap<>();

        if (Files.isDirectory(GATEWAY_DIR)) {
            final var gatewayReport = gatewayReadingService.read(GATEWAY_DIR);
            final ComparisonResult gatewayComparisonResult = comparisonService.compare(xrvReport, gatewayReport);
            sheets.put("jqa_gateway", reportRowMapper.toExcelRows(gatewayComparisonResult.getRecords(), "JQA"));

            System.out.println("Comparison with jqa_gateway completed. Matched rows: "
                    + gatewayComparisonResult.getSuccessCount() + ", mismatched rows: "
                    + gatewayComparisonResult.getFailedCount());
        } else {
            System.out.println("Directory not found: " + GATEWAY_DIR + ". Skipping sheet jqa_gateway.");
        }

        if (Files.isDirectory(VENDOR_TOOL_DIR)) {
            final var vendorToolReport = vendorToolReadingService.read(VENDOR_TOOL_DIR);
            final ComparisonResult vendorComparisonResult = comparisonService.compare(
                    xrvReport,
                    vendorToolReport,
                    "vendor_tool"
            );
            sheets.put("vendor_tool", reportRowMapper.toExcelRows(vendorComparisonResult.getRecords(), "Vendor Tool"));

            System.out.println("Comparison with vendor_tool completed. Matched rows: "
                    + vendorComparisonResult.getSuccessCount() + ", mismatched rows: "
                    + vendorComparisonResult.getFailedCount());
        } else {
            System.out.println("Directory not found: " + VENDOR_TOOL_DIR + ". Skipping sheet vendor_tool.");
        }

        if (sheets.isEmpty()) {
            System.out.println("No comparison sheets to generate. Report file was not created.");
            return;
        }

        reportService.writeToExcelFile(sheets, REPORT_FILE);
        System.out.println("Combined report generated: " + REPORT_FILE.getAbsolutePath());
    }
}
