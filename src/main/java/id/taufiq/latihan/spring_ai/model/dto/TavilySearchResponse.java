package id.taufiq.latihan.spring_ai.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TavilySearchResponse {
    private String query;
    private String answer;
    private List<String> images;
    private List<Result> results;
    private String responseTime;
    private AutoParameters autoParameters;
    private String requestId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Result {
        private String title;
        private String url;
        private String content;
        private Double score;
        private String rawContent;
        private String favicon;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AutoParameters {
        private String topic;
        private String searchDepth;
    }
}
