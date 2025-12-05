package id.taufiq.latihan.spring_ai.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Component
public final class JsonCsvConverter {

    private final ObjectMapper objectMapper;

    public JsonCsvConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Convert a JSON object or array of objects into CSV (UTF-8 friendly).
     * Field order follows the first object and new fields are appended as they appear.
     */
    public String toCsv(String json) {
        if (json == null || json.isBlank()) {
            return "";
        }

        try {
            JsonNode root = objectMapper.readTree(json);
            List<JsonNode> rows = new ArrayList<>();

            if (root.isArray()) {
                root.forEach(rows::add);
            } else {
                rows.add(root);
            }

            if (rows.isEmpty()) {
                return "";
            }

            LinkedHashSet<String> headers = new LinkedHashSet<>();
            for (JsonNode row : rows) {
                if (row.isObject()) {
                    row.fieldNames().forEachRemaining(headers::add);
                }
            }

            if (headers.isEmpty()) {
                return "";
            }

            List<String> headerList = new ArrayList<>(headers);
            StringBuilder sb = new StringBuilder();
            appendLine(sb, headerList);

            for (JsonNode row : rows) {
                List<String> values = new ArrayList<>();
                for (String header : headerList) {
                    values.add(asCell(row.get(header)));
                }
                appendLine(sb, values);
            }

            return sb.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid JSON input for CSV conversion", e);
        }
    }

    private void appendLine(StringBuilder sb, List<String> cells) {
        for (int i = 0; i < cells.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(escape(cells.get(i)));
        }
        sb.append('\n');
    }

    private String asCell(JsonNode node) {
        if (node == null || node.isNull()) {
            return "";
        }
        if (node.isTextual()) {
            return node.textValue();
        }
        if (node.isNumber() || node.isBoolean()) {
            return node.asText();
        }
        // Collapse nested structures to JSON string to keep CSV valid.
        return node.toString();
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        boolean needsQuote = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        String escaped = value.replace("\"", "\"\"");
        return needsQuote ? "\"" + escaped + "\"" : escaped;
    }
}
