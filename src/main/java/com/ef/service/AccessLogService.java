package com.ef.service;

import com.ef.entity.AccessLog;
import com.ef.repository.AccessLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccessLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogService.class);

    @Value("${spring.jpa.properties.hibernate.batch_size}")
    private int batchSize;
    private List<AccessLog> accessLogList = new ArrayList<>();

    private AccessLogRepository logRepository;

    public AccessLogService(AccessLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void persist(AccessLog accessLog) {
        accessLogList.add(accessLog);

        if (accessLogList.size() == batchSize) {
            flushCache();
        }
    }

    public void flushCache() {
        logRepository.bulkPersist(accessLogList);
        LOGGER.info("Bulk persisted {} rows", accessLogList.size());
        accessLogList.clear();
    }
}