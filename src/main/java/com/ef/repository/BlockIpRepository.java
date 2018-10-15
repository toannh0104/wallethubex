package com.ef.repository;

import com.ef.entity.BlockedIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Inquiry and block ipaddr
 */

@Repository
public interface BlockIpRepository extends JpaRepository<BlockedIp, Long> {

    @Modifying
    @Query(value = "INSERT INTO BlockedIp(ip, comments) "
            + "SELECT log.ipAddress, "
            + "CONCAT('Exceeded threshold(',:threshold,').  Number of request: ', count(*)) AS comments "
            + "FROM AccessLog log "
            + "WHERE log.date BETWEEN :fromDate AND :toDate "
            + "GROUP BY log.ipAddress "
            + "HAVING COUNT(*)>:threshold")
    public void scanLogToBlock(@Param("fromDate") LocalDateTime fromDate,
                               @Param("toDate") LocalDateTime toDate,
                               @Param("threshold") long threshold);
}