package id.taufiq.latihan.spring_ai.service;

import id.taufiq.latihan.spring_ai.model.entity.UserRateLimit;
import id.taufiq.latihan.spring_ai.repository.UserRateLimitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class RateLimiterService {

    private static final int MAX_REQUESTS = 10;
    private final UserRateLimitRepository rateLimitRepository;

    public RateLimiterService(UserRateLimitRepository rateLimitRepository) {
        this.rateLimitRepository = rateLimitRepository;
    }

    public UserRateLimit checkAndConsume(String username) {
        LocalDate today = LocalDate.now();
        UserRateLimit rateLimit = rateLimitRepository.findById(username)
                .orElseGet(() -> new UserRateLimit(username, MAX_REQUESTS, 0, today));

        if (rateLimit.getRequestDate() == null || !rateLimit.getRequestDate().isEqual(today)) {
            rateLimit.setRequestCount(0);
            rateLimit.setRequestDate(today);
        }

        if (rateLimit.getRequestCount() >= rateLimit.getMaxRequests()) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded");
        }

        rateLimit.setRequestCount(rateLimit.getRequestCount() + 1);
        rateLimitRepository.save(rateLimit);
        return rateLimit;
    }
}
