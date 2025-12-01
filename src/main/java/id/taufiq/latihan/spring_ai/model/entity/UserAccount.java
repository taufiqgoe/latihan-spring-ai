package id.taufiq.latihan.spring_ai.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserAccount {
    @Id
    private Long id;
    private String username;
    private String password;
    private String roles; // comma separated roles
    private boolean enabled;
}
