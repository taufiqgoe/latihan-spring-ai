package id.taufiq.latihan.spring_ai.repository;

import id.taufiq.latihan.spring_ai.model.entity.WebSearchHistory;
import id.taufiq.latihan.spring_ai.util.Mapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WebSearchHistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public WebSearchHistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public WebSearchHistory save(WebSearchHistory history) {
        String sql = """
                INSERT INTO web_search_histories (id, query, raw_request, raw_response)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (id) DO UPDATE
                    SET query = EXCLUDED.query,
                        raw_request = EXCLUDED.raw_request,
                        raw_response = EXCLUDED.raw_response
                """;
        jdbcTemplate.update(sql, history.getId(), history.getQuery(), history.getRawRequest(), history.getRawResponse());
        return history;
    }

    public Optional<WebSearchHistory> findById(String id) {
        String sql = "SELECT id, query, raw_request, raw_response FROM web_search_histories WHERE id = ?";
        return jdbcTemplate.query(sql, Mapper.WEB_SEARCH_HISTORY_ROW_MAPPER, id).stream().findFirst();
    }
}
