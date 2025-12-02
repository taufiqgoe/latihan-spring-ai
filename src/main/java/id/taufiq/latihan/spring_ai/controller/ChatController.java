package id.taufiq.latihan.spring_ai.controller;

import id.taufiq.latihan.spring_ai.service.ChatService;
import id.taufiq.latihan.spring_ai.service.IndexService;
import id.taufiq.latihan.spring_ai.service.RateLimiterService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
public class ChatController {

    private final ChatClient openAiChatClient;
    private final IndexService indexService;
    private final ChatService chatService;
    private final RateLimiterService rateLimiterService;

    public ChatController(ChatClient openAiChatClient, IndexService indexService, ChatService chatService, RateLimiterService rateLimiterService) {
        this.openAiChatClient = openAiChatClient;
        this.indexService = indexService;
        this.chatService = chatService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping("chat")
    public ResponseEntity<String> chat(@RequestBody MessageDto messageDto, @RequestParam String tenant, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "anonymous";
        var limitState = rateLimiterService.checkAndConsume(username);

        String response = chatService.chat(messageDto.getMessage(), tenant);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RateLimit-Limit", String.valueOf(limitState.getMaxRequests()));
        headers.add("X-RateLimit-Remaining", String.valueOf(Math.max(0, limitState.getMaxRequests() - limitState.getRequestCount())));
        return ResponseEntity.ok().headers(headers).body(response);
    }

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageDto index(@RequestPart("file") MultipartFile file, @RequestParam String tenant) {
        indexService.index(file, tenant);
        return new MessageDto("success");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageDto {
        private String message;
    }
}
