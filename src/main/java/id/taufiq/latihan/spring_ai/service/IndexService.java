package id.taufiq.latihan.spring_ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.taufiq.latihan.spring_ai.exception.UnsupportedFileFormatException;
import id.taufiq.latihan.spring_ai.model.dto.FileUploadedEvent;
import id.taufiq.latihan.spring_ai.util.ObjectStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.io.Resource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndexService {

    private static final Logger log = LoggerFactory.getLogger(IndexService.class);

    private final VectorStore vectorStore;
    private final ObjectStorage objectStorage;
    private final ObjectMapper objectMapper;

    public IndexService(VectorStore vectorStore, ObjectStorage objectStorage, ObjectMapper objectMapper) {
        this.vectorStore = vectorStore;
        this.objectStorage = objectStorage;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${app.kafka.topics.file-upload}", groupId = "${app.kafka.consumer.group-id:indexer}")
    public void handle(String payload) {
        try {
            FileUploadedEvent event = objectMapper.readValue(payload, FileUploadedEvent.class);
            Resource resource = objectStorage.download(event.getBucket(), event.getKey());
            index(resource, event.getTenant(), event.getOriginalFilename());
            log.info("Indexed file {} for tenant {}", event.getKey(), event.getTenant());
        } catch (Exception e) {
            log.error("Failed to index file payload {}", payload, e);
            throw new IllegalStateException("Failed to process upload event", e);
        }
    }

    public void index(Resource resource, String tenant, String originalFilename) {
        try {
            String filename = originalFilename != null ? originalFilename : resource.getFilename();
            validatePdf(filename);
            TikaDocumentReader pdfReader = new TikaDocumentReader(resource);
            TokenTextSplitter textSplitter = new TokenTextSplitter();
            List<Document> documents = pdfReader.get();
            for (Document document : documents) {
                document.getMetadata().put("tenant", tenant.toLowerCase());
            }

            FilterExpressionBuilder filterExpressionBuilder = new FilterExpressionBuilder();
            FilterExpressionBuilder.Op filterTenant = filterExpressionBuilder.eq("tenant", tenant);
            FilterExpressionBuilder.Op filterSource = filterExpressionBuilder.eq("source", filename);
            vectorStore.delete(filterExpressionBuilder.and(filterTenant, filterSource).build());

            vectorStore.accept(textSplitter.apply(documents));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to index uploaded file", e);
        }
    }

    private void validatePdf(String filename) {
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            throw new UnsupportedFileFormatException("File format is not supported. Only PDF files are allowed.");
        }
    }
}
