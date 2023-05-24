package com.comparison.xrv;

import java.nio.file.Path;

import com.comparison.xrv.service.GatewayHtmlReadingService;
import com.comparison.xrv.service.HtmlReadingService;
import com.comparison.xrv.service.VendorToolHtmlReadingService;
import com.comparison.xrv.service.XrvHtmlReadingService;

public class Test {
    public static void main(final String[] args) {
        final HtmlReadingService gatewayReadingService = new GatewayHtmlReadingService();
        final HtmlReadingService xrvService = new XrvHtmlReadingService();
        final HtmlReadingService vendorToolService = new VendorToolHtmlReadingService();

        final var gatewayReport = gatewayReadingService.read(Path.of(
                "src/main/resources/jqa_gateway"
        ));
        final var xrvReport = xrvService.read(Path.of(
                "src/main/resources/xrv_service"
        ));
        final var vendorToolReport = vendorToolService.read(Path.of(
                "src/main/resources/vendor_tool"
        ));
    }
}
