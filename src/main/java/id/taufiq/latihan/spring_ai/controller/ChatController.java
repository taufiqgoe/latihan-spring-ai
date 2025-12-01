package id.taufiq.latihan.spring_ai.controller;

import id.taufiq.latihan.spring_ai.service.ChatService;
import id.taufiq.latihan.spring_ai.service.IndexService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ChatController {

    private final ChatClient openAiChatClient;
    private final IndexService indexService;
    private final ChatService chatService;

    public ChatController(ChatClient openAiChatClient, IndexService indexService, ChatService chatService) {
        this.openAiChatClient = openAiChatClient;
        this.indexService = indexService;
        this.chatService = chatService;
    }

    @PostMapping("chat")
    public String chat(@RequestBody MessageDto messageDto, @RequestParam String tenant) {
        return chatService.chat(messageDto.getMessage(), tenant);
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
