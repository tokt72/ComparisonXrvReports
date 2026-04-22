# ComparisonXrvReports

A utility for comparing XRV reports (`xrv_service`) with external sources:
- `jqa_gateway`
- `vendor_tool`

The result is generated as a single Excel file with separate sheets per source.

## What The Project Does

1. Reads HTML reports from `src/main/resources/xrv_service`.
2. Optionally reads reports from:
- `src/main/resources/jqa_gateway`
- `src/main/resources/vendor_tool`
3. For each available source, compares records against XRV using:
- `level`
- `ruleId`
- `line`
- `column`
- normalized `message` text
4. Generates an Excel report: `target/report/COMPARISON_REPORT.xlsx`.

If a source directory is missing, the corresponding Excel sheet is not created.
If both directories (`jqa_gateway` and `vendor_tool`) are missing, no report is generated.

## Input Data Structure

Expected directories:
- `src/main/resources/xrv_service` (required)
- `src/main/resources/jqa_gateway` (optional)
- `src/main/resources/vendor_tool` (optional)

### How `articleId` Is Determined

`articleId` is taken from the filename up to the first `_` (or up to the extension if `_` is absent), then converted to uppercase.

Examples:
- `RCM1_XRVReport.html` -> `RCM1`
- `rcm4_resp.html` -> `RCM4`
- `RCM1.html` -> `RCM1`

## Output File

Report file:
- `target/report/COMPARISON_REPORT.xlsx`

Sheets:
- `jqa_gateway` - only if `src/main/resources/jqa_gateway` exists
- `vendor_tool` - only if `src/main/resources/vendor_tool` exists

Columns in each sheet:
- `Result`
- `ArticleId`
- `Level`
- `Rule`
- `Line`
- `Column`
- `Message XRV`
- `Message <Source>`
- `Message Diff`
- `Source`

## Key Classes

Entry point:
- `src/main/java/com/comparison/xrv/Test.java`

Reading services:
- `src/main/java/com/comparison/xrv/service/XrvHtmlReadingService.java`
- `src/main/java/com/comparison/xrv/service/GatewayHtmlReadingService.java`
- `src/main/java/com/comparison/xrv/service/VendorToolHtmlReadingService.java`
- `src/main/java/com/comparison/xrv/service/BaseHtmlReadingService.java`
- `src/main/java/com/comparison/xrv/service/SeparatingHtmlReadingService.java`

Comparison and report preparation:
- `src/main/java/com/comparison/xrv/service/ComparisonService.java`
- `src/main/java/com/comparison/xrv/service/MessageDiffService.java`
- `src/main/java/com/comparison/xrv/service/ComparisonReportRowMapper.java`
- `src/main/java/com/comparison/xrv/service/ReportService.java`

Models:
- `src/main/java/com/comparison/xrv/model/`

## Requirements

- Java 17+
- Maven 3.8+
- OS: Windows/Linux/macOS

## How To Run

### 1. Build

```bash
mvn -DskipTests compile
```

### 2. Run The Main Class

#### Windows PowerShell

```powershell
mvn "-Dexec.mainClass=com.comparison.xrv.Test" "-Dexec.classpathScope=runtime" org.codehaus.mojo:exec-maven-plugin:3.1.0:java
```

#### Linux/macOS (bash/zsh)

```bash
mvn -Dexec.mainClass=com.comparison.xrv.Test -Dexec.classpathScope=runtime org.codehaus.mojo:exec-maven-plugin:3.1.0:java
```

After execution, check:
- `target/report/COMPARISON_REPORT.xlsx`

## Expected Console Messages

Example:
- `Comparison with jqa_gateway completed. Matched rows: ..., mismatched rows: ...`
- `Comparison with vendor_tool completed. Matched rows: ..., mismatched rows: ...`
- `Combined report generated: ...\target\report\COMPARISON_REPORT.xlsx`

If a directory is not found:
- `Directory not found: ... Skipping sheet jqa_gateway.`
- `Directory not found: ... Skipping sheet vendor_tool.`

If both sources are missing:
- `No comparison sheets to generate. Report file was not created.`

## Quick Validation

1. Make sure `src/main/resources/xrv_service` exists.
2. Add at least one source: `jqa_gateway` or `vendor_tool`.
3. Run the command from "How To Run".
4. Open `target/report/COMPARISON_REPORT.xlsx` and verify sheet count:
- 2 sheets if both directories exist.
- 1 sheet if only one directory exists.

## Dependencies (pom.xml)

Main libraries:
- `jsoup` - HTML parsing
- `poi-ooxml` - Excel generation
- `lombok` - model classes
- `guava`, `opencsv`, `log4j-core`

---

If needed, adding a new report source is straightforward: implement `HtmlReadingService`, wire it in `Test.java`, and add a dedicated sheet to the `sheets` map.