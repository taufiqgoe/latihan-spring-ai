package id.taufiq.latihan.spring_ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.taufiq.latihan.spring_ai.exception.UnsupportedFileFormatException;
import id.taufiq.latihan.spring_ai.model.dto.MessageDto;
import id.taufiq.latihan.spring_ai.model.dto.FileUploadedEvent;
import id.taufiq.latihan.spring_ai.model.dto.StoredObject;
import id.taufiq.latihan.spring_ai.util.ObjectStorage;
import id.taufiq.latihan.spring_ai.util.MessageBroker;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ChatService {

    private final ChatClient openAiChatClient;
    private final VectorStore vectorStore;
    private final RateLimiterService rateLimiterService;
    private final ObjectStorage objectStorage;
    private final ObjectMapper objectMapper;
    private final MessageBroker messageBroker;
    private final String fileUploadTopic;
    private final String bucketName;

    public ChatService(ChatClient openAiChatClient,
                       VectorStore vectorStore,
                       RateLimiterService rateLimiterService,
                       ObjectStorage objectStorage,
                       MessageBroker messageBroker,
                       ObjectMapper objectMapper,
                       Environment environment) {
        this.openAiChatClient = openAiChatClient;
        this.vectorStore = vectorStore;
        this.rateLimiterService = rateLimiterService;
        this.objectStorage = objectStorage;
        this.messageBroker = messageBroker;
        this.objectMapper = objectMapper;
        this.fileUploadTopic = environment.getRequiredProperty("app.kafka.topics.file-upload");
        this.bucketName = environment.getRequiredProperty("app.s3.bucket-name");
    }

    public String chat(String userMessage, String tenant) {
        FilterExpressionBuilder filterExpressionBuilder = new FilterExpressionBuilder();
        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .filterExpression(filterExpressionBuilder.eq("tenant", tenant).build())
                        .build())
                .build();

        return openAiChatClient.prompt()
                .user(userMessage)
                .advisors(questionAnswerAdvisor)
                .call()
                .content();
    }

    public MessageDto handleUpload(MultipartFile file, String tenant) {
        validatePdf(file.getOriginalFilename());
        StoredObject stored = objectStorage.upload(file, bucketName);
        FileUploadedEvent event = new FileUploadedEvent(
                stored.getBucket(),
                stored.getKey(),
                tenant.toLowerCase(),
                stored.getOriginalFilename());
        messageBroker.send(fileUploadTopic, toJson(event));
        return new MessageDto("accepted");
    }

    private String toJson(FileUploadedEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize upload event", e);
        }
    }

    private void validatePdf(String filename) {
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            throw new UnsupportedFileFormatException("File format is not supported. Only PDF files are allowed.");
        }
    }
}
