package id.taufiq.latihan.spring_ai.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TavilyExtractRequest {
    private String urls;
    private Boolean includeImages;
    private Boolean includeFavicon;
    private String extractDepth;
    private String format;
    private String timeout;
}
