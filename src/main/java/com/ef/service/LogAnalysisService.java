package com.ef.service;


import com.ef.entity.BlockedIp;
import com.ef.repository.BlockIpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class LogAnalysisService {

    private BlockIpRepository blockIpRepository;

    @Autowired
    public LogAnalysisService(BlockIpRepository blockIpRepository) {
        this.blockIpRepository = blockIpRepository;
    }

    @Transactional
    public void analysisAssessLog(LocalDateTime startTime, TimeUnit timeUnit, int threshold) {
        LocalDateTime endTime = LocalDateTime.from(startTime).plusHours(timeUnit.toHours(1)).minusSeconds(1);

        blockIpRepository.scanLogToBlock(startTime, endTime, threshold);
    }

    public List<BlockedIp> findBlockedIps() {
        return blockIpRepository.findAll();
    }

}