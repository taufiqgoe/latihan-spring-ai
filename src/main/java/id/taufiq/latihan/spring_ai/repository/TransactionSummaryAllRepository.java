package id.taufiq.latihan.spring_ai.repository;

import id.taufiq.latihan.spring_ai.config.TransactionSummaryStatus;
import id.taufiq.latihan.spring_ai.model.entity.TransactionSummaryAll;
import id.taufiq.latihan.spring_ai.util.Mapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionSummaryAllRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionSummaryAllRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TransactionSummaryAll> findByMerchantIdAndStatusAndDateRange(String merchantId, TransactionSummaryStatus status, LocalDate start, LocalDate end) {
        StringBuilder sql = new StringBuilder("SELECT * FROM v_transaction_summary_all WHERE trans_status = ? AND transaction_date BETWEEN ? AND ?");

        var params = new ArrayList<>();
        params.add(status.name());
        params.add(start);
        params.add(end);

        if (merchantId != null && !merchantId.isBlank()) {
            sql.append(" AND merchant_unique_code = ?");
            params.add(merchantId);
        }

        return jdbcTemplate.query(sql.toString(), Mapper.TRANSACTION_SUMMARY_ALL_ROW_MAPPER, params.toArray());
    }
}
