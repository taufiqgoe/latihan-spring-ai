package id.taufiq.latihan.spring_ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.taufiq.latihan.spring_ai.model.dto.TavilyExtractRequest;
import id.taufiq.latihan.spring_ai.model.dto.TavilyExtractResponse;
import id.taufiq.latihan.spring_ai.model.dto.TavilySearchRequest;
import id.taufiq.latihan.spring_ai.model.dto.TavilySearchResponse;
import id.taufiq.latihan.spring_ai.model.entity.WebSearchHistory;
import id.taufiq.latihan.spring_ai.repository.WebSearchHistoryRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class WebSearchService {

    private final String tavilyToken;
    private final WebSearchHistoryRepository webSearchHistoryRepository;
    private final ObjectMapper objectMapper;

    public WebSearchService(Environment environment, WebSearchHistoryRepository webSearchHistoryRepository, ObjectMapper objectMapper) {
        this.tavilyToken = environment.getProperty("app.tavily.token");
        this.webSearchHistoryRepository = webSearchHistoryRepository;
        this.objectMapper = objectMapper;
    }

    public TavilySearchResponse searchTavily(TavilySearchRequest requestBody) {
        if (tavilyToken == null) {
            throw new IllegalStateException("Please set tavily.token");
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tavilyToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TavilySearchRequest> request = new HttpEntity<>(requestBody, headers);
        HttpEntity<TavilySearchResponse> response = restTemplate.postForEntity(
                "https://api.tavily.com/search",
                request,
                TavilySearchResponse.class);

        try {
            webSearchHistoryRepository.save(
                    new WebSearchHistory(UUID.randomUUID().toString(),
                            requestBody.getQuery(),
                            objectMapper.writeValueAsString(request),
                            objectMapper.writeValueAsString(response)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }

    public TavilyExtractResponse extractTavily(TavilyExtractRequest requestBody, String token) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

//        // Example body
//        TavilyExtractRequest.builder()
//                .urls("https://en.wikipedia.org/wiki/Artificial_intelligence")
//                .includeImages(false)
//                .includeFavicon(false)
//                .extractDepth("basic")
//                .format("markdown")
//                .timeout("None")
//                .build();

        HttpEntity<TavilyExtractRequest> request = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForObject(
                "https://api.tavily.com/extract",
                request,
                TavilyExtractResponse.class);
    }
}
