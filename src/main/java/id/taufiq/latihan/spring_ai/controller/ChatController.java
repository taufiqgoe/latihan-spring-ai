package id.taufiq.latihan.spring_ai.controller;

import id.taufiq.latihan.spring_ai.service.IndexService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ChatController {

    private final ChatClient openAiChatClient;
    private final IndexService indexService;

    public ChatController(ChatClient openAiChatClient, IndexService indexService) {
        this.openAiChatClient = openAiChatClient;
        this.indexService = indexService;
    }

    @PostMapping("chat")
    public String chat(@RequestBody MessageDto messageDto) {
        String content = openAiChatClient.prompt().user(messageDto.getMessage()).call().content();
//        return new MessageDto(content);
        return content;
    }

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageDto index(@RequestPart("file") MultipartFile file) {
        indexService.index(file);
        return new MessageDto("success");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageDto {
        private String message;
    }
}
