package id.taufiq.latihan.spring_ai.repository;

import id.taufiq.latihan.spring_ai.model.entity.UserRateLimit;
import id.taufiq.latihan.spring_ai.repository.mapper.Mappers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public class UserRateLimitRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRateLimitRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<UserRateLimit> findById(String username) {
        String sql = "SELECT username, max_requests, request_count, request_date FROM user_rate_limits WHERE username = ?";
        return jdbcTemplate.query(sql, Mappers.USER_RATE_LIMIT_ROW_MAPPER, username).stream().findFirst();
    }

    public UserRateLimit save(UserRateLimit rateLimit) {
        String sql = """
                INSERT INTO user_rate_limits (username, max_requests, request_count, request_date)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (username) DO UPDATE
                    SET max_requests = EXCLUDED.max_requests,
                        request_count = EXCLUDED.request_count,
                        request_date = EXCLUDED.request_date
                """;
        jdbcTemplate.update(sql,
                rateLimit.getUsername(),
                rateLimit.getMaxRequests(),
                rateLimit.getRequestCount(),
                Date.valueOf(rateLimit.getRequestDate()));
        return rateLimit;
    }

}
