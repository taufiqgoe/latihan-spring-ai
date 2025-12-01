package id.taufiq.latihan.spring_ai.service;

import id.taufiq.latihan.spring_ai.model.entity.AuthToken;
import id.taufiq.latihan.spring_ai.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class TokenService {

    private static final int TOKEN_BYTE_LENGTH = 32;

    private final SecureRandom secureRandom = new SecureRandom();
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(String username) {
        byte[] randomBytes = new byte[TOKEN_BYTE_LENGTH];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        AuthToken authToken = new AuthToken(token, username);
        authToken.setCreatedAt(LocalDateTime.now());
        tokenRepository.save(authToken);
        return token;
    }

    public String getUsername(String token) {
        return tokenRepository.findById(token)
                .map(AuthToken::getUsername)
                .orElse(null);
    }

    public void revoke(String token) {
        tokenRepository.deleteById(token);
    }
}
