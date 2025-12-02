package id.taufiq.latihan.spring_ai.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("user_rate_limits")
public class UserRateLimit {

    @Id
    private String username;
    @Column("max_requests")
    private int maxRequests;
    @Column("request_count")
    private int requestCount;
    @Column("request_date")
    private LocalDate requestDate;
}
