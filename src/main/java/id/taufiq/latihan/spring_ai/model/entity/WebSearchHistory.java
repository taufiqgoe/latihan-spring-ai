package id.taufiq.latihan.spring_ai.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("web_search_histories")
public class WebSearchHistory {
    @Id
    private String id;
    private String query;
    private String rawRequest;
    private String rawResponse;
}
