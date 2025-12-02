package id.taufiq.latihan.spring_ai.repository.mapper;

import id.taufiq.latihan.spring_ai.model.entity.AuthToken;
import id.taufiq.latihan.spring_ai.model.entity.UserAccount;
import id.taufiq.latihan.spring_ai.model.entity.UserRateLimit;
import org.springframework.jdbc.core.RowMapper;

public final class Mappers {

    private Mappers() {
    }

    public static final RowMapper<AuthToken> AUTH_TOKEN_ROW_MAPPER = (rs, rowNum) -> new AuthToken(
            rs.getString("token"),
            rs.getString("username"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public static final RowMapper<UserAccount> USER_ACCOUNT_ROW_MAPPER = (rs, rowNum) -> new UserAccount(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("roles"),
            rs.getBoolean("enabled")
    );

    public static final RowMapper<UserRateLimit> USER_RATE_LIMIT_ROW_MAPPER = (rs, rowNum) -> new UserRateLimit(
            rs.getString("username"),
            rs.getInt("max_requests"),
            rs.getInt("request_count"),
            rs.getDate("request_date").toLocalDate()
    );
}
