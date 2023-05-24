package com.comparison.xrv.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.comparison.xrv.model.ValidationRow;

public interface HtmlReadingService {

    List<ValidationRow> read(Path path);
}
