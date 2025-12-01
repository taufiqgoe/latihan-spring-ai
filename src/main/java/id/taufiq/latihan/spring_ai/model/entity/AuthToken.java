package id.taufiq.latihan.spring_ai.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table("tokens")
public class AuthToken implements Persistable<String> {

    @Id
    private String token;
    private String username;
    private LocalDateTime createdAt;

    @Transient
    private boolean isNew = true;

    public AuthToken(String token, String username) {
        this.token = token;
        this.username = username;
    }

    @PersistenceCreator
    public AuthToken(String token, String username, LocalDateTime createdAt) {
        this.token = token;
        this.username = username;
        this.createdAt = createdAt;
        this.isNew = false;
    }

    @Override
    public String getId() {
        return token;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
