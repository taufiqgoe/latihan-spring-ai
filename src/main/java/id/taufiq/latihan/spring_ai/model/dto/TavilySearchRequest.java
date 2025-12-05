package id.taufiq.latihan.spring_ai.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TavilySearchRequest {
    private String query;
    private Boolean autoParameters;
    private String topic;
    private String searchDepth;
    private Integer chunksPerSource;
    private Integer maxResults;
    private String timeRange;
    private String startDate;
    private String endDate;
    private Boolean includeAnswer;
    private Boolean includeRawContent;
    private Boolean includeImages;
    private Boolean includeImageDescriptions;
    private Boolean includeFavicon;
    private List<String> includeDomains;
    private List<String> excludeDomains;
    private String country;
}
