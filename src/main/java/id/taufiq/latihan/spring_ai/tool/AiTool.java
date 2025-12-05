package id.taufiq.latihan.spring_ai.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.taufiq.latihan.spring_ai.config.TransactionSummaryStatus;
import id.taufiq.latihan.spring_ai.model.dto.TavilySearchRequest;
import id.taufiq.latihan.spring_ai.model.dto.TavilySearchResponse;
import id.taufiq.latihan.spring_ai.model.entity.TransactionSummaryAll;
import id.taufiq.latihan.spring_ai.repository.TransactionSummaryAllRepository;
import id.taufiq.latihan.spring_ai.service.WebSearchService;
import id.taufiq.latihan.spring_ai.util.JsonCsvConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class AiTool {

    private static final Logger log = LoggerFactory.getLogger(AiTool.class);
    private final TransactionSummaryAllRepository transactionSummaryAllRepository;
    private final JsonCsvConverter jsonCsvConverter;
    private final ObjectMapper objectMapper;
    private final WebSearchService webSearchService;

    public AiTool(TransactionSummaryAllRepository transactionSummaryAllRepository, JsonCsvConverter jsonCsvConverter, ObjectMapper objectMapper, WebSearchService webSearchService) {
        this.transactionSummaryAllRepository = transactionSummaryAllRepository;
        this.jsonCsvConverter = jsonCsvConverter;
        this.objectMapper = objectMapper;
        this.webSearchService = webSearchService;
    }

    @Tool(description = "Provided Transaction data filtered By Merchant Id, Status, Date")
    public String findByMerchantIdAndStatusAndDateRange(
            @ToolParam(description = "The merchant ids", required = false) String merchantId,
            @ToolParam(description = "Enum of the transaction status, Input must be: SETTLED, VOID, EXPIRED_QRIS, PENDING, REVERSE, DECLINED, SUCCESSFUL") TransactionSummaryStatus status,
            @ToolParam(description = "Transaction date start, Input must be in format ISO-8601 extended local date format yyyy-MM-dd") String start,
            @ToolParam(description = "Transaction date end, Input must be in format ISO-8601 extended local date format yyyy-MM-dd") String end) {
        log.info("Calling tool findByMerchantIdAndStatusAndDateRange");
        List<TransactionSummaryAll> transactions = transactionSummaryAllRepository.findByMerchantIdAndStatusAndDateRange(merchantId, status, LocalDate.parse(start), LocalDate.parse(end));
        String csv;
        try {
            csv = jsonCsvConverter.toCsv(objectMapper.writeValueAsString(transactions));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return csv;
    }

    @Tool(description = "A web-search tool to get information from internet. Only use it when up-to-date information is truly needed")
    public String webSearch(
            @ToolParam(description = "The search query to execute.")
            String query,
            @ToolParam(description = "The category of the search. Available options: general, news, finance")
            String topic,
            @ToolParam(description = "The time range back from the current date to filter results based on publish date or last updated date. Useful when looking for sources that have published or updated data. Available options: day, week, month, year, d, w, m, y", required = false)
            String timeRange,
            @ToolParam(description = "Will return all results after the specified start date based on publish date or last updated date. Required to be written in the format YYYY-MM-DD", required = false)
            String startDate,
            @ToolParam(description = "Will return all results before the specified end date based on publish date or last updated date. Required to be written in the format YYYY-MM-DD", required = false)
            String endDate) {
        log.info("Calling tool webSearch");
        TavilySearchRequest request = TavilySearchRequest.builder()
                .query(query)
                .autoParameters(false)
                .topic(topic)
                .searchDepth("basic")
                .chunksPerSource(3)
                .maxResults(1)
                .timeRange(timeRange)
                .startDate(startDate)
                .endDate(endDate)
                .includeAnswer(true)
                .includeRawContent(false)
                .includeImages(false)
                .includeImageDescriptions(false)
                .includeFavicon(false)
                .includeDomains(Collections.emptyList())
                .excludeDomains(Collections.emptyList())
                .country(null)
                .build();

        TavilySearchResponse response = webSearchService.searchTavily(request);

        return response.getAnswer();
    }

    @Tool(description = "Get the current date and time in the user's timezone")
    public String getCurrentDateTime() {
        log.info("Calling tool getCurrentDateTime");
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
