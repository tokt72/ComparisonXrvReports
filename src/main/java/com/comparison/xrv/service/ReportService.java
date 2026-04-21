package com.comparison.xrv.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ReportService {

    private static final String COMMA = ",";
    private static final String DEFAULT_SEPARATOR = COMMA;
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMBEDDED_DOUBLE_QUOTES = "\"\"";
    private static final String NEW_LINE_UNIX = "\n";
    private static final String NEW_LINE_WINDOWS = "\r\n";

    public String convertToCsvFormat(final String[] line) {
        return convertToCsvFormat(line, DEFAULT_SEPARATOR);
    }

    public String convertToCsvFormat(final String[] line, final String separator) {
        return convertToCsvFormat(line, separator, true);
    }

    public String convertToCsvFormat(
            final String[] line,
            final String separator,
            final boolean quote) {

        return Stream.of(line)
                .map(l -> formatCsvField(l, quote))
                .collect(Collectors.joining(separator));
    }

    private String formatCsvField(final String field, final boolean quote) {
        String result = field;

        if (result.contains(COMMA)
                || result.contains(DOUBLE_QUOTES)
                || result.contains(NEW_LINE_UNIX)
                || result.contains(NEW_LINE_WINDOWS)) {

            result = result.replace(DOUBLE_QUOTES, EMBEDDED_DOUBLE_QUOTES);

            result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;

        } else {
            if (quote) {
                result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;
            }
        }

        return result;
    }

    public void writeToCsvFile(List<String[]> data, File file) {
        try {
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (FileWriter writer = new FileWriter(file)) {
                for (String[] row : data) {
                    writer.write(String.join(",", row));
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToExcelFile(final List<String[]> data, final File file) {
        try {
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (Workbook workbook = new XSSFWorkbook(); FileOutputStream outputStream = new FileOutputStream(file)) {
                final Sheet sheet = workbook.createSheet("Comparison");

                final CellStyle headerStyle = createHeaderStyle(workbook);
                final CellStyle bodyStyle = createBodyStyle(workbook);

                for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                    final Row row = sheet.createRow(rowIndex);
                    final String[] values = data.get(rowIndex);

                    for (int colIndex = 0; colIndex < values.length; colIndex++) {
                        final Cell cell = row.createCell(colIndex);
                        cell.setCellValue(values[colIndex]);
                        cell.setCellStyle(rowIndex == 0 ? headerStyle : bodyStyle);
                    }
                }

                if (!data.isEmpty()) {
                    final int lastColumn = data.get(0).length - 1;
                    sheet.setAutoFilter(new CellRangeAddress(0, data.size() - 1, 0, lastColumn));
                    sheet.createFreezePane(0, 1);

                    for (int colIndex = 0; colIndex <= lastColumn; colIndex++) {
                        sheet.autoSizeColumn(colIndex);
                        int width = sheet.getColumnWidth(colIndex);
                        sheet.setColumnWidth(colIndex, Math.min(width + 600, 90 * 256));
                    }

                    for (int rowIndex = 1; rowIndex < data.size(); rowIndex++) {
                        sheet.getRow(rowIndex).setHeight((short) -1);
                    }
                }

                workbook.write(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CellStyle createHeaderStyle(final Workbook workbook) {
        final CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createBodyStyle(final Workbook workbook) {
        final CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }
}
