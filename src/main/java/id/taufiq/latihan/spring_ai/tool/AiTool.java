package id.taufiq.latihan.spring_ai.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.taufiq.latihan.spring_ai.config.TransactionSummaryStatus;
import id.taufiq.latihan.spring_ai.model.entity.TransactionSummaryAll;
import id.taufiq.latihan.spring_ai.repository.TransactionSummaryAllRepository;
import id.taufiq.latihan.spring_ai.util.JsonCsvConverter;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AiTool {

    private final TransactionSummaryAllRepository transactionSummaryAllRepository;
    private final JsonCsvConverter jsonCsvConverter;
    private final ObjectMapper objectMapper;

    public AiTool(TransactionSummaryAllRepository transactionSummaryAllRepository, JsonCsvConverter jsonCsvConverter, ObjectMapper objectMapper) {
        this.transactionSummaryAllRepository = transactionSummaryAllRepository;
        this.jsonCsvConverter = jsonCsvConverter;
        this.objectMapper = objectMapper;
    }

    @Tool(description = "Provided Transaction data filtered By Merchant Id, Status, Date")
    public String findByMerchantIdAndStatusAndDateRange(
            @ToolParam(description = "The merchant ids", required = false) String merchantId,
            @ToolParam(description = "Enum of the transaction status, Input must be: SETTLED, VOID, EXPIRED_QRIS, PENDING, REVERSE, DECLINED, SUCCESSFUL") TransactionSummaryStatus status,
            @ToolParam(description = "Transaction date start, Input must be in format ISO-8601 extended local date format yyyy-MM-dd") String start,
            @ToolParam(description = "Transaction date end, Input must be in format ISO-8601 extended local date format yyyy-MM-dd") String end) {
        List<TransactionSummaryAll> transactions = transactionSummaryAllRepository.findByMerchantIdAndStatusAndDateRange(merchantId, status, LocalDate.parse(start), LocalDate.parse(end));
        String csv;
        try {
            csv = jsonCsvConverter.toCsv(objectMapper.writeValueAsString(transactions));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return csv;
    }

    @Tool(description = "Get the current date and time in the user's timezone")
    public String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
