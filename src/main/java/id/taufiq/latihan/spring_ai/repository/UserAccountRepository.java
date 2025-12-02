package id.taufiq.latihan.spring_ai.repository;

import id.taufiq.latihan.spring_ai.model.entity.UserAccount;
import id.taufiq.latihan.spring_ai.repository.mapper.Mappers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserAccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<UserAccount> findByUsername(String username) {
        String sql = "SELECT id, username, password, roles, enabled FROM users WHERE username = ?";
        return jdbcTemplate.query(sql, Mappers.USER_ACCOUNT_ROW_MAPPER, username).stream().findFirst();
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(1) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

}
