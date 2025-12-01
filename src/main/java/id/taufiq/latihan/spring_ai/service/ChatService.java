package id.taufiq.latihan.spring_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient openAiChatClient;
    private final VectorStore vectorStore;

    public ChatService(ChatClient openAiChatClient, VectorStore vectorStore) {
        this.openAiChatClient = openAiChatClient;
        this.vectorStore = vectorStore;
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
}
