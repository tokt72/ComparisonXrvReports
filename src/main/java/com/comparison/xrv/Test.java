package com.comparison.xrv;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

import com.comparison.xrv.model.ValidationRow;
import com.comparison.xrv.service.GatewayHtmlReadingService;
import com.comparison.xrv.service.HtmlReadingService;
import com.comparison.xrv.service.HtmlReadingServiceImpl;

public class Test {

    public static void main(String[] args) {
        HtmlReadingService gatewayReadingService = new GatewayHtmlReadingService();
        HtmlReadingServiceImpl htmlReadingService = new HtmlReadingServiceImpl();
        HashMap<Integer, List<String>> valuesFromXrv = new HashMap<>();
        Map<Integer, List<ValidationRow>> valuesFromJqa;

//        try {
//            valuesFromXrv = htmlReadingService.readFileFromXrv(1,
//                    "C:\\projects\\ComparisonXrvReports\\src\\main\\resources\\xrv_service\\rcm9_resp.html");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        valuesFromJqa = gatewayReadingService.read(Path.of(
                "C:\\projects\\ComparisonXrvReports\\src\\main\\resources\\jqa_gateway\\RCM9_XRVReport.html"
        ));
        System.out.println("fgdbdfgdf");
    }
}
