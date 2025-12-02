package id.taufiq.latihan.spring_ai.controller;

import id.taufiq.latihan.spring_ai.model.dto.MessageDto;
import id.taufiq.latihan.spring_ai.model.entity.UserRateLimit;
import id.taufiq.latihan.spring_ai.service.ChatService;
import id.taufiq.latihan.spring_ai.service.RateLimiterService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ChatController {

    private final ChatService chatService;
    private final RateLimiterService rateLimiterService;

    public ChatController(ChatService chatService, RateLimiterService rateLimiterService) {
        this.chatService = chatService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping("chat")
    public ResponseEntity<String> chat(@RequestBody MessageDto messageDto, @RequestParam String tenant, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "anonymous";
        UserRateLimit limitState = rateLimiterService.checkAndConsume(username);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RateLimit-Limit", String.valueOf(limitState.getMaxRequests()));
        headers.add("X-RateLimit-Remaining", String.valueOf(Math.max(0, limitState.getMaxRequests() - limitState.getRequestCount())));

        String response = chatService.chat(messageDto.getMessage(), tenant);
        return ResponseEntity.ok().headers(headers).body(response);
    }

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageDto upload(@RequestPart("file") MultipartFile file, @RequestParam String tenant) {
        return chatService.handleUpload(file, tenant);
    }
}
