package com.ef.repository;

import com.ef.entity.AccessLog;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * AccessLog repository
 */
@Repository
public class AccessLogRepository {

    private JdbcTemplate template;

    public AccessLogRepository(JdbcTemplate template) {
        this.template = template;
    }

    /**
     * Bulk insert AccessLog
     * @param entities
     */
    @Transactional
    public void bulkPersist(final List<AccessLog> entities) {
        template.batchUpdate("insert into access_log (date, ip_address) values (?, ?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, entities.get(i).getDate().toString());
                ps.setString(2, entities.get(i).getIpAddress());
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        });
    }

}
