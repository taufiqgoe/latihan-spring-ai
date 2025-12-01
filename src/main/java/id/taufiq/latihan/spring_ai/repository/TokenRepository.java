package id.taufiq.latihan.spring_ai.repository;

import id.taufiq.latihan.spring_ai.model.entity.AuthToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<AuthToken, String> {
}
