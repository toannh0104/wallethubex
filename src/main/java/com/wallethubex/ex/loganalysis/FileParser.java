package com.wallethubex.ex.loganalysis;

import com.wallethubex.ex.loganalysis.entity.AccessLog;
import com.wallethubex.ex.loganalysis.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FileParser {
    private static final int DATE_INDEX = 0;
    private static final int IP_INDEX = 1;

    @Autowired
    private AccessLogService persistService;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public void importAccessLog(File logFile) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(logFile.toURI()));
        lines.forEach(line -> {
            String[] arr = line.split("\\|");
            LocalDateTime dd = LocalDateTime.parse(arr[DATE_INDEX], TIME_FORMATTER);
            //Timestamp eventTime = Timestamp.valueOf(dd);
            AccessLog accessLog = new AccessLog(dd, arr[IP_INDEX]);
            persistService.persist(accessLog);
        });

        persistService.flushCache();
    }
}