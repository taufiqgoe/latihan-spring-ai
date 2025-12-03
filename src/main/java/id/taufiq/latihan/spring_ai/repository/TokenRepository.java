package id.taufiq.latihan.spring_ai.repository;

import id.taufiq.latihan.spring_ai.model.entity.AuthToken;
import id.taufiq.latihan.spring_ai.util.Mapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class TokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public TokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AuthToken save(AuthToken token) {
        String sql = """
                INSERT INTO tokens (token, username, created_at)
                VALUES (?, ?, ?)
                ON CONFLICT (token) DO UPDATE
                    SET username = EXCLUDED.username,
                        created_at = EXCLUDED.created_at
                """;
        jdbcTemplate.update(sql, token.getToken(), token.getUsername(), Timestamp.valueOf(token.getCreatedAt()));
        return token;
    }

    public Optional<AuthToken> findById(String token) {
        String sql = "SELECT token, username, created_at FROM tokens WHERE token = ?";
        return jdbcTemplate.query(sql, Mapper.AUTH_TOKEN_ROW_MAPPER, token).stream().findFirst();
    }

    public void deleteById(String token) {
        jdbcTemplate.update("DELETE FROM tokens WHERE token = ?", token);
    }

}
